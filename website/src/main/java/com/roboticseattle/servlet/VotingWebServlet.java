package com.roboticseattle.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.roboticseattle.model.Votes;
import java.nio.file.Files;

@WebServlet(urlPatterns="/voting")
public class VotingWebServlet extends HttpServlet {
	
	private static final long serialVersionUID = 2652899739555484926L;

	private static Gson gson = new Gson();
	private static Votes votes;
	
	static {
		File f = new File(System.getProperty("user.home")+"/repo/");
		if (f.exists() && f.isDirectory()) {
		   try(BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/repo/votes.json"))) {
			   String line = br.readLine();
			   if(line != null) {
				   votes = gson.fromJson(line, Votes.class);
			   }
		   } catch(IOException ioe) {
			   ioe.printStackTrace();
			   votes = new Votes();
		   }
		} else {
			f.mkdirs();
            votes = new Votes();
		}
	}
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(votes));
        out.close();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		votes.add(request.getParameter("button"));
	}

}
