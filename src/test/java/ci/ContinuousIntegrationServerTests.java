package ci;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


import org.json.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.util.stream.Collectors;
import org.json.*;
import java.nio.file.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class ContinuousIntegrationServerTests {
    Request baseRequest;
    HttpServletRequest request;
    HttpServletResponse response;
    JSONObject requestBodyJson;

    @Test
  	@DisplayName("Check for build success")
  	void checkBuildSuccessForRepo() {
          requestBodyJson = new JSONObject("{\"head_commit\": {\"id\": \"7bb8b28ba9d8ae15c063f784ba72ed606a3d344a\"}, \"repository\": {\"clone_url\": \"https://github.com/MichaelaSahlgren/DD2480.git\"},\"author\":{\"name\":\"Name\",\"email\":\"test@mail.com\",\"username\":\"UserName\"}}");
          String result = ContinuousIntegrationServer.processWebhookCommit(requestBodyJson);
  		assertTrue(result.contains("BUILD SUCCESS"));
  	}

    @Test
    @DisplayName("Clone example repo and run tests")
  	void cloneAndTestRepoTest() {
          String repoUrl = "https://github.com/MichaelaSahlgren/DD2480.git";
          String commitId = "7bb8b28ba9d8ae15c063f784ba72ed606a3d344a";
          String result = ContinuousIntegrationServer.cloneAndTest(repoUrl, commitId);
  		assertTrue(result.contains("BUILD SUCCESS"));
  	}

    //Tests for sending email notification
    @Test
    //If build succeeds
    void sendEmailTestingSuccess(){
      requestBodyJson = new JSONObject("{\"head_commit\": {\"id\": \"7bb8b28ba9d8ae15c063f784ba72ed606a3d344a\", \"committer\":{\"name\":\"Name\",\"email\":\"sara.damne@gmail.com\",\"username\":\"UserName\"}}, \"repository\": {\"clone_url\": \"https://github.com/MichaelaSahlgren/DD2480.git\"}}");

      assertTrue(ContinuousIntegrationServer.sendGmail(requestBodyJson, "BUILD SUCCESS"));
    }
    @Test
    //If build fails
    void sendEmailTestingFailure(){
      requestBodyJson = new JSONObject("{\"head_commit\": {\"id\": \"7bb8b28ba9d8ae15c063f784ba72ed606a3d344a\", \"committer\":{\"name\":\"Name\",\"email\":\"sara.damne@gmail.com\",\"username\":\"UserName\"}}, \"repository\": {\"clone_url\": \"https://github.com/MichaelaSahlgren/DD2480.git\"}}");

      assertTrue(ContinuousIntegrationServer.sendGmail(requestBodyJson, "BUILD FAILURE"));
    }

}
