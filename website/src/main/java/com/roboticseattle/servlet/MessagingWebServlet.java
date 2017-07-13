package com.roboticseattle.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roboticseattle.model.Votes;

@WebServlet(urlPatterns="/msg")
public class MessagingWebServlet extends HttpServlet {
	
	private static final long serialVersionUID = -714398337426009622L;

	static File f = new File(System.getProperty("user.home")+"/repo/msg");

	static {
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File newMsgFileName = new File(f, System.currentTimeMillis()+".txt");
        try(BufferedWriter out = new BufferedWriter(new FileWriter(newMsgFileName))) {
        	out.write(request.getParameter("txt")+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
}
