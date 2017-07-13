package com.roboticseattle.servlet;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roboticseattle.mind.ElectronicBrain;

@WebServlet(urlPatterns="/sound")
public class SoundWebServlet extends HttpServlet{
	
	private ElectronicBrain electronicBrain;
	
	@Override
	public void init() {
		
		electronicBrain = ElectronicBrain.getBrain();
	}
	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	System.out.println("electronicBrain.isMicrophoneOn()="+electronicBrain.isMicrophoneOn());
		
        try
        {
            response.setContentType("audio/wav");
            ServletOutputStream out = response.getOutputStream();
            while(electronicBrain.isMicrophoneOn()) {
            	byte[] chunk = electronicBrain.wavChunk();
        		while(chunk != null) {
        			out.write(chunk);
        			out.flush();
                	chunk = electronicBrain.wavChunk();
        		}
        		Thread.sleep(25);
            }
        	System.out.println("electronicBrain.isMicrophoneOn()="+electronicBrain.isMicrophoneOn());
    		out.close();
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
        }
        
    }
	

}
