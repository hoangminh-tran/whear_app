package com.tttm.Whear.App.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.utils.request.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TextFileReader {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    private final ClothesService clothesService;
    private final PostService postService;
    private final SubroleService subroleService;
    private final StyleService styleService;
    private final BodyShapeService bodyShapeService;
    private final AuthenticationService authenticationService;
    private final RuleMatchingClothesService ruleMatchingClothesService;

    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            String run = null;
//            String run = "run";
            if (run != null) {
                readSubRoleFromFile();
                readStyleFromFile();
                readBodyShapeFromFile();
                readRuleMatchingClothesFromFile();
                readUsersFromFile();
                readPostFromFile();
                readClothesFromFile();
            }
        } catch (IOException | CustomException e) {
            e.printStackTrace();
        }
    }

    private void readStyleFromFile() throws IOException, CustomException {
        List<StyleRequest> styleRequests = readStyleRequests("classpath:data/styles.txt");
        for (StyleRequest styleRequest : styleRequests) {
            styleService.createStyle(styleRequest);
            System.out.println(styleRequest);
        }
    }

    private List<StyleRequest> readStyleRequests(String filePath) throws IOException {
        List<StyleRequest> styleRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        styleRequests.add(convertToStyleRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last clothes request if there is any remaining data
            if (stringBuilder.length() > 0) {
                styleRequests.add(convertToStyleRequest(stringBuilder.toString()));
            }
        }
        return styleRequests;
    }

    private StyleRequest convertToStyleRequest(String json) throws IOException {
        return objectMapper.readValue(json, StyleRequest.class);
    }

    private void readBodyShapeFromFile() throws IOException, CustomException {
        List<BodyShapeRequest> bodyShapeRequests = readBodyShapeRequests("classpath:data/body_shapes.txt");
        for (BodyShapeRequest bodyShapeRequest : bodyShapeRequests) {
            bodyShapeService.createBodyShape(bodyShapeRequest);
            System.out.println(bodyShapeRequest);
        }
    }

    private List<BodyShapeRequest> readBodyShapeRequests(String filePath) throws IOException {
        List<BodyShapeRequest> bodyShapeRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        bodyShapeRequests.add(convertToBodyShapeRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last body shape request if there is any remaining data
            if (stringBuilder.length() > 0) {
                bodyShapeRequests.add(convertToBodyShapeRequest(stringBuilder.toString()));
            }
        }
        return bodyShapeRequests;
    }

    private BodyShapeRequest convertToBodyShapeRequest(String json) throws IOException {
        return objectMapper.readValue(json, BodyShapeRequest.class);
    }

    private void readRuleMatchingClothesFromFile() throws IOException, CustomException {
        List<RuleMatchingClothesRequest> ruleMatchingClothesRequests = readRuleMatchingClothesRequests("classpath:data/rule_matching_clothes.txt");
        for (RuleMatchingClothesRequest ruleMatchingClothesRequest : ruleMatchingClothesRequests) {
            ruleMatchingClothesService.createNewRule(ruleMatchingClothesRequest);
            System.out.println(ruleMatchingClothesRequest);
        }
    }

    private List<RuleMatchingClothesRequest> readRuleMatchingClothesRequests(String filePath) throws IOException {
        List<RuleMatchingClothesRequest> ruleMatchingClothesRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        ruleMatchingClothesRequests.add(convertToRuleMatchingClothesRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last rule matching clothes request if there is any remaining data
            if (stringBuilder.length() > 0) {
                ruleMatchingClothesRequests.add(convertToRuleMatchingClothesRequest(stringBuilder.toString()));
            }
        }
        return ruleMatchingClothesRequests;
    }

    private RuleMatchingClothesRequest convertToRuleMatchingClothesRequest(String json) throws IOException {
        return objectMapper.readValue(json, RuleMatchingClothesRequest.class);
    }


    private void readUsersFromFile() throws IOException, CustomException {
        List<UserRequest> userRequests = readUsersRequests("classpath:data/users.txt");
        for (UserRequest userRequest : userRequests) {
            authenticationService.register(userRequest);
            System.out.println(userRequests);
        }
    }

    private List<UserRequest> readUsersRequests(String filePath) throws IOException {
        List<UserRequest> userRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        userRequests.add(convertToUsersRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last clothes request if there is any remaining data
            if (stringBuilder.length() > 0) {
                userRequests.add(convertToUsersRequest(stringBuilder.toString()));
            }
        }
        return userRequests;
    }

    private UserRequest convertToUsersRequest(String json) throws IOException {
        return objectMapper.readValue(json, UserRequest.class);
    }

    private void readSubRoleFromFile() throws IOException, CustomException {
        List<SubRoleRequest> subRoleRequests = readSubRoleRequests("classpath:data/sub_role.txt");
        for (SubRoleRequest subRoleRequest : subRoleRequests) {
            subroleService.createSubRole(subRoleRequest);
            System.out.println(subRoleRequest);
        }
    }

    private List<SubRoleRequest> readSubRoleRequests(String filePath) throws IOException {
        List<SubRoleRequest> subRoleRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        subRoleRequests.add(convertToSubRoleRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last clothes request if there is any remaining data
            if (stringBuilder.length() > 0) {
                subRoleRequests.add(convertToSubRoleRequest(stringBuilder.toString()));
            }
        }
        return subRoleRequests;
    }

    private SubRoleRequest convertToSubRoleRequest(String json) throws IOException {
        return objectMapper.readValue(json, SubRoleRequest.class);
    }

    private void readClothesFromFile() throws IOException, CustomException {
        List<ClothesRequest> clothesRequests = readClothesRequests("classpath:data/clothes.txt");
        for (ClothesRequest clothesRequest : clothesRequests) {
            clothesService.createClothes(clothesRequest);
            System.out.println(clothesRequest);
        }
    }

    private List<ClothesRequest> readClothesRequests(String filePath) throws IOException {
        List<ClothesRequest> clothesRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        clothesRequests.add(convertToClothesRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last clothes request if there is any remaining data
            if (stringBuilder.length() > 0) {
                clothesRequests.add(convertToClothesRequest(stringBuilder.toString()));
            }
        }
        return clothesRequests;
    }

    private ClothesRequest convertToClothesRequest(String json) throws IOException {
        return objectMapper.readValue(json, ClothesRequest.class);
    }

    private void readPostFromFile() throws IOException, CustomException {
        List<PostRequest> postRequests = readPostRequests("classpath:data/posts.txt");
        for (PostRequest postRequest : postRequests) {
            postService.createPost(postRequest);
            System.out.println(postRequests);
        }
    }

    private List<PostRequest> readPostRequests(String filePath) throws IOException {
        List<PostRequest> postRequests = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains("--------------------------------------")) {
                    if (stringBuilder.length() > 0) {
                        postRequests.add(convertToPostsRequest(stringBuilder.toString()));
                        stringBuilder.setLength(0);
                    }
                } else {
                    stringBuilder.append(line);
                }
            }
            // Add the last clothes request if there is any remaining data
            if (stringBuilder.length() > 0) {
                postRequests.add(convertToPostsRequest(stringBuilder.toString()));
            }
        }
        return postRequests;
    }

    private PostRequest convertToPostsRequest(String json) throws IOException {
        return objectMapper.readValue(json, PostRequest.class);
    }
}
