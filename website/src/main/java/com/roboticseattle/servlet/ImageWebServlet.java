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
import com.roboticseattle.socket.spar.SparWebSocket;

@WebServlet(urlPatterns="/image/*")
public class ImageWebServlet extends HttpServlet {
	
	private ElectronicBrain electronicBrain;
	
	@Override
	public void init() {
		electronicBrain = ElectronicBrain.getBrain();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		InputStream inputStream = null;
        try
        {
            response.setContentType("image/jpeg");
            ServletOutputStream out = response.getOutputStream();
           	inputStream = new ByteArrayInputStream(electronicBrain.getJpeg());
    		int len=0;
    		byte[] chunk = new byte[4096];
    		while((len=inputStream.read(chunk)) != -1) {
    			out.write(chunk, 0, len);
    			out.flush();
    		}
    		out.close();
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        } finally {
        	if(inputStream != null) inputStream.close();
        }
        
    }
	

}
