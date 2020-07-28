package com.magicbussines.VeMecApi;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class VeMecApiApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(VeMecApiApplication.class, args);
	}
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VeMecApiApplication.class);
    }
	
	public static String ReadPath(String type) {

		try {
			String readed = "";
			//MODIFICAR ESTO PARA LA LECTURA DE LAS RUTAS
			Path path = Paths.get("/usr/local/tomcat/webapps/Vemecs/Routes.txt");
			
			if(type.equals("pdf")) {
			    readed = Files.readAllLines(path).get(0); 
			}else {
				readed = Files.readAllLines(path).get(1);
			}			
			return readed;
		    
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	public static String getPdfPath() {
		return ReadPath("pdf");
	}
	public static String getChartPath() {
		return ReadPath("Charts");
	}
	
	
}
