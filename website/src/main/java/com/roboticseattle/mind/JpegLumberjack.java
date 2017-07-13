package com.roboticseattle.mind;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * The class cuts the incoming stream of jpeg files glued together into the separate jpeg files
 * The idea is to hop from one FF marker to another until failing to read a valid marker and then start reading byte-by-byte till FFD9
 * @author RoboticSeattle
 *
 */
public class JpegLumberjack {
	/* The state of the lumberjack can be one of the following */
	public enum State {
		STARTING_FF, // FF followed by D8
		SKIPPING_FORWARD, 
		D8, FF, MARKER, LEN, // Jumping Markers
		BYTE_BY_BYTE, 
		D9, // Found the end of JPEG
		READING_MORE_BYTES
	}
	
    /**
	 * Single jpeg cannot be larger than 64K otherwise you'll have ArrayIndexOutOfBoundsException on your hands
	 */
	public JpegLumberjack() {
		stateStack.push(State.STARTING_FF);
		buffer = new byte[65536];
	}
	
	/**
	 * Single jpeg cannot be larger than initial capacity otherwise you'll have ArrayIndexOutOfBoundsException on your hands
	 */
	public JpegLumberjack(int capacity) {
		stateStack.push(State.STARTING_FF);
		buffer = new byte[capacity];
	}
	
	/**
	 * Feed the bytes read from the stream here and wait until it returns equals to D9 
	 * @param chunk
	 * @param len
	 * @return
	 */
	public State feed(byte[] chunk, int len) throws InvalidJpegException {
		try {
		  System.arraycopy(chunk, 0, buffer, buffReadIndex, len);
		} catch(ArrayIndexOutOfBoundsException aie) {
			throw new InvalidJpegException("buffReadIndex="+buffReadIndex+"; len="+len, aie);
		}
		buffReadIndex += len;
		if(state() == State.READING_MORE_BYTES) {
			if(buffReadIndex >= targetBuffReadIndex) { popState(); targetBuffReadIndex = 0; }
			else return state();
		}
		try {
			while(state() != State.D9) {
				switch(state()) {
				  case STARTING_FF: if(isNext((byte) 0xFF)) state(State.D8);
				                    else throw new InvalidJpegException("Expected starting 'FF' but was '"+byteAt(buffJpegIndex)+"'");
				                    break;
				  case D8: if(isNext((byte) 0xD8)) {state(State.FF);}
				           else throw new InvalidJpegException("Expected 'D8' but was '"+byteAt(buffJpegIndex)+"'");
				           break;
				  case FF: if(isNext((byte) 0xFF)) {state(State.MARKER);}
				           else {
				        	   state(State.BYTE_BY_BYTE); 
				               if(marker != (byte) 0xDA) throw new InvalidJpegException("Expected 'DA' but was '"+Integer.toHexString(marker & 0xFF)+"' byte read="+byteAt(buffJpegIndex)+" index="+buffJpegIndex); 
				               break;
				           } 
				           // No break slipping right into MARKER
				  case MARKER: marker = next(); if(noLenMarkers.contains(marker)) {state(State.FF); break;} else state(State.LEN); // No break slipping into LEN reading 
				  case LEN: len2Skip = wrap(next(), oneForward()); state(State.SKIPPING_FORWARD); // No break slipping into SKIPPING_FORWARD phase
				  case SKIPPING_FORWARD: skip(len2Skip-1); state(State.FF); break; 
				  case BYTE_BY_BYTE: while(buffJpegIndex < buffReadIndex-1) {if(buffer[buffJpegIndex++] == (byte)0xFF && 
						                                                        buffer[buffJpegIndex] == (byte)0xD9) {
					                                                              state(State.D9); 
						                                                          break;}}
				                     throw new NotEnoughDataException(buffReadIndex+1);
				  default: throw new IllegalStateException("The unexpected state: "+state());
				}
			}
		} catch(NotEnoughDataException nede) {
			// Nothing to do here as it is perfectly expected as it waits for the new data to come ...
		}
		return state();
	}
	
	/**
	 * As soon as feed(...) == State.D9 DO NOT FORGET to read a valid JPEG from here. Otherwise the buffer may overflow
	 * @return - valid JPEG bytes
	 */
	public byte[] read() {
		byte[] retBytes = null;
		if(state() == State.D9) {
			retBytes = Arrays.copyOf(buffer, buffJpegIndex+1);
			int excessLen = buffReadIndex-(buffJpegIndex+1);
			if(excessLen >0) {
			    System.arraycopy(buffer, buffJpegIndex+1, buffer, 0, excessLen);
			    buffReadIndex=excessLen;
			} else {
				buffReadIndex=0;
			}
			buffJpegIndex = 0;
			marker = 0;
			len2Skip = 0;
			targetBuffReadIndex = 0;
			four2Wrap[2] = 0x00; four2Wrap[3] = 0x00;
			state(State.STARTING_FF);
		}
        return retBytes;
	}
	
	private static Set<Byte> noLenMarkers = new HashSet<>(Arrays.asList(new Byte[]{(byte)0x00, (byte)0x01, (byte)0xD0, (byte)0xD1, (byte)0xD2, (byte)0xD3, (byte)0xD4, (byte)0xD5, (byte)0xD6, (byte)0xD7}));
	
	private byte[] buffer; 
	private int    buffReadIndex = 0;
	private int    buffJpegIndex = 0;
	private byte   marker = 0;
	private int    len2Skip = 0;;
	private int    targetBuffReadIndex = 0;
	private byte[] four2Wrap = new byte[] {0x00,0x00,0x00,0x00};
	
	private Stack<State> stateStack = new Stack<>();
	
	private State state() {
		return stateStack.peek();
	}
	
	private State state(State state) {
		State prev = null;
		if(stateStack.size() > 0) { prev = stateStack.pop(); }
		stateStack.push(state);
		return prev;
	}

	private State pushState(State state) {
		State prev = null;
		if(stateStack.size() > 0) { prev = stateStack.peek(); }
		stateStack.push(state);
		return prev;
	}
	
	private State popState() {
		return stateStack.pop();
	}
	
	private class NotEnoughDataException extends Exception {
		private static final long serialVersionUID = 3009402351611409358L;
		
		public NotEnoughDataException(int aTarget) {
			targetBuffReadIndex = aTarget; 
			if(state() != State.READING_MORE_BYTES) pushState(State.READING_MORE_BYTES);
		}
	}
	
	private String byteAt(int index) {
		return Integer.toHexString(buffer[index] & 0xFF);
	}
	
	private int wrap(byte byte1, byte byte2) {
		four2Wrap[2] = byte1;
		four2Wrap[3] = byte2;
		return ByteBuffer.wrap(four2Wrap, 0, 4).getInt();
	}
	
	private void skip(int target) throws NotEnoughDataException {
		if(buffReadIndex > buffJpegIndex+target) {
			buffJpegIndex += target;
		} else {
			throw new NotEnoughDataException(buffJpegIndex+target);
		}
	}
	
	private byte next() throws NotEnoughDataException {
		if(buffReadIndex > buffJpegIndex) {
		    return buffer[buffJpegIndex++];
		} else {
		    throw new NotEnoughDataException(buffJpegIndex+1);	
		}
	}
	
	private byte oneForward() throws NotEnoughDataException {
	    return buffer[buffJpegIndex];
	}
	
	private boolean isNext(byte expectedByte) throws NotEnoughDataException {
		if(buffReadIndex > buffJpegIndex) {
			if(buffer[buffJpegIndex] == expectedByte) {
				buffJpegIndex++;
				return true;
			} else {
                return false;				
			}
		} else {
			throw new NotEnoughDataException(buffJpegIndex+1);	
		}
	}
	
	public static void main(String[] args) throws Exception {
		InputStream inputStream = new FileInputStream("C:/bigmovie/test.jpg");
		
		JpegLumberjack lumberjack = new JpegLumberjack();
		
		byte[] chunk = new byte[1024];
		int len;
		int i=0;
		while((len=inputStream.read(chunk)) != -1) {
			JpegLumberjack.State state = lumberjack.feed(chunk, len); 
			if(state == JpegLumberjack.State.D9) {
    			Path pathOut = Paths.get("C:/bigmovie/splitter/lubberjack"+i+".jpg");
	    		Files.write(pathOut, lumberjack.read());
				i++;
			}	
		}
		
		inputStream.close();
		
	}

}
