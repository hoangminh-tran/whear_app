package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.*;
import com.tttm.Whear.App.enums.ClothesType;
import com.tttm.Whear.App.enums.MaterialType;
import com.tttm.Whear.App.enums.ShapeType;
import com.tttm.Whear.App.enums.TypeOfPosts;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesRepository;
import com.tttm.Whear.App.repository.CommentsRepostitory;
import com.tttm.Whear.App.repository.ReactRepository;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.utils.request.ClothesRequest;
import com.tttm.Whear.App.utils.request.PostRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.CommentsResponse;
import com.tttm.Whear.App.utils.response.PostResponse;
import com.tttm.Whear.App.utils.response.ReactResponse;
import com.tttm.Whear.App.utils.response.UserResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClothesServiceImpl implements ClothesService {

    private final ClothesRepository clothesRepository;
    private final ClothesImageService clothesImageService;
    private final ClothesSizeService clothesSizeService;
    private final ClothesColorService clothesColorService;
    private final ClothesSeasonService clothesSeasonService;
    private final ClothesStyleService clothesStyleService;
    private final PostService postService;
    private final HashtagService hashtagService;
    private final CommentsRepostitory commentService;
    private final ReactRepository reactService;
    private final UserService userService;
    private final ClothesGenderService clothesGenderService;

    @Override
    public ClothesResponse createClothes(ClothesRequest clothesRequest) throws CustomException {
        PostResponse post = null;
        Clothes newClothes = null;
        if (clothesRequest.getUserID() == null
                || clothesRequest.getUserID().isBlank()
                || clothesRequest.getUserID().isEmpty()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        try {
            PostRequest postRequest = PostRequest.builder()
                    .userID(clothesRequest.getUserID())
                    .date(LocalDateTime.now())
                    .hashtag(clothesRequest.getHashtag())
                    .typeOfPosts(TypeOfPosts.CLOTHES)
                    .status("PUCLIC")
                    .build();
            post = postService.createPost(postRequest);

            if (post == null) {
                throw new CustomException(ConstantMessage.CREATE_FAIL.getMessage());
            }

            Clothes clothes = Clothes.builder()
                    .clothesID(post.getPostID())
                    .posts(postService.getPostEntityByPostID(post.getPostID()))
                    .nameOfProduct(clothesRequest.getNameOfProduct())
                    .typeOfClothes(ClothesType.valueOf(clothesRequest.getTypeOfClothes().toUpperCase()))
                    .shape(ShapeType.valueOf(clothesRequest.getShape().toUpperCase()))
                    .description(clothesRequest.getDescription())
                    .link(clothesRequest.getLink())
                    .rating(clothesRequest.getRating())
                    .materials(MaterialType.valueOf(clothesRequest.getMaterials().toUpperCase()))
                    .build();

            clothesRepository.insertClothes(
                    post.getPostID(), clothesRequest.getDescription(), clothesRequest.getLink(),
                    clothesRequest.getMaterials(), clothesRequest.getNameOfProduct(),
                    clothesRequest.getRating(), clothesRequest.getShape(),
                    clothesRequest.getTypeOfClothes()
            );
            newClothes = clothesRepository.getClothesByClothesID(post.getPostID());
            if (newClothes == null) {
                throw new CustomException(ConstantMessage.CREATE_FAIL.getMessage());
            }

            List<String> clothesImages = clothesRequest.getClothesImages();
            if (clothesImages != null && !clothesImages.isEmpty() && clothesImages.size() > 0) {
                for (String image : clothesImages) {
                    clothesImageService.createImage(post.getPostID(), image);
                }
            }

            List<String> clothesSizes = clothesRequest.getClothesSizes();
            if (clothesSizes != null && !clothesSizes.isEmpty() && clothesSizes.size() > 0) {
                for (String size : clothesSizes) {
                    ClothesSize finded = clothesSizeService.findByName(post.getPostID(), size);
                    if (finded == null) {
                        clothesSizeService.createSize(post.getPostID(), size.toUpperCase());
                    }
                }
            }

            List<String> clothesSeasons = clothesRequest.getClothesSeasons();
            if (clothesSeasons != null && !clothesSeasons.isEmpty() && clothesSeasons.size() > 0) {
                for (String season : clothesSeasons) {
                    ClothesSeason finded = clothesSeasonService.findByName(post.getPostID(), season);
                    if (finded == null) {
                        clothesSeasonService.createSeason(post.getPostID(), season);
                    }
                }
            }

            List<String> clothesColors = clothesRequest.getClothesColors();
            if (clothesColors != null && !clothesColors.isEmpty() && clothesColors.size() > 0) {
                for (String color : clothesColors) {
                    ClothesColor finded = clothesColorService.findByName(post.getPostID(), color);
                    if (finded == null) {
                        clothesColorService.createColor(post.getPostID(), color.toUpperCase());
                    }
                }
            }

            List<String> clothesStyle = clothesRequest.getClothesStyle();
            if (clothesStyle != null && !clothesStyle.isEmpty() && clothesStyle.size() > 0) {
                for (String style : clothesStyle) {
                  ClothesColor finded = clothesColorService.findByName(post.getPostID(), style);
                  if (finded == null) {
                    clothesStyleService.createStyle(post.getPostID(), style.toUpperCase());
                  }
                }
            }

            List<String> clothesGender = clothesRequest.getClothesGender();
            if (clothesGender != null && !clothesGender.isEmpty() && clothesGender.size() > 0) {
                for (String gender : clothesGender) {
                  ClothesColor finded = clothesColorService.findByName(post.getPostID(), gender);
                  if (finded == null) {
                    clothesGenderService.createGender(post.getPostID(), gender.toUpperCase());
                  }
                }
            }
            return mapToClothesResponse(newClothes);

        } catch (Exception ex) {
            if (post != null) {
                postService.deletePostByPostID(post.getPostID());
            }
            if (newClothes != null) {
                clothesRepository.deleteById(newClothes.getClothesID());
                clothesImageService.deleteByClothesID(newClothes.getClothesID());
            }
            throw ex;
        }
    }

    @Override
    public List<ClothesResponse> getAllClothes() throws CustomException {
        List<Clothes> clothesList = clothesRepository.findAll();

        List<ClothesResponse> responseList = new ArrayList<>();
        for (Clothes clothes : clothesList) {
            System.out.println("=============================================");
            System.out.println(clothes.getClothesID());
            ClothesResponse clothesResponse = mapToClothesResponse(clothes);
            responseList.add(clothesResponse);
        }

        return responseList;
    }

    @Override
    public ClothesResponse getClothesByID(Integer clothesID) throws CustomException {
        if (clothesID == null || clothesID.toString().isEmpty() || clothesID.toString().isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        Clothes clothes = clothesRepository.getClothesByClothesID(clothesID);
        if (clothes == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }
        return mapToClothesResponse(clothes);
    }

    @Override
    public Clothes getClothesEntityByID(Integer clothesID) throws CustomException {
        if (clothesID == null || clothesID.toString().isEmpty() || clothesID.toString().isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        Clothes clothes = clothesRepository.getClothesByClothesID(clothesID);
        return clothes;
    }

    @Override
    public ClothesResponse updateClothes(ClothesRequest clothesRequest) throws CustomException {
        if (clothesRequest == null || clothesRequest.getClothesID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        if (clothesRequest.getClothesID().toString().isBlank() || clothesRequest.getClothesID()
                .toString().isEmpty()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        Clothes clothes = clothesRepository.findById(clothesRequest.getClothesID()).get();
        if (clothes == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }

        clothes.setTypeOfClothes(
                ClothesType.valueOf(clothesRequest.getTypeOfClothes().trim().toUpperCase()));
        clothes.setLink(
                clothesRequest.getLink()
        );
        clothes.setMaterials(
                MaterialType.valueOf(clothesRequest.getMaterials().trim().toUpperCase())
        );
        clothes.setDescription(
                clothesRequest.getDescription()
        );
        clothes.setNameOfProduct(
                clothesRequest.getNameOfProduct()
        );
        clothes.setShape(
                ShapeType.valueOf(clothesRequest.getShape().trim().toUpperCase())
        );
        clothes.setRating(clothesRequest.getRating());
        clothesRepository.save(clothes);

        clothesImageService.deleteByClothesID(clothes.getClothesID());
        clothesSizeService.deleteByClothesID(clothes.getClothesID());
        clothesSeasonService.deleteByClothesID(clothes.getClothesID());
        clothesColorService.deleteByClothesID(clothes.getClothesID());
        clothesStyleService.deleteByClothesID(clothes.getClothesID());
        clothesGenderService.deleteByClothesID(clothes.getClothesID());

        List<String> clothesImages = clothesRequest.getClothesImages();
        if (clothesImages != null && !clothesImages.isEmpty() && clothesImages.size() > 0) {
            for (String image : clothesImages) {
                clothesImageService.createImage(clothes.getClothesID(), image);
            }
        }

        List<String> clothesSizes = clothesRequest.getClothesSizes();
        if (clothesSizes != null && !clothesSizes.isEmpty() && clothesSizes.size() > 0) {
            for (String size : clothesSizes) {
                ClothesSize finded = clothesSizeService.findByName(clothes.getClothesID(), size);
                if (finded == null) {
                    clothesSizeService.createSize(clothes.getClothesID(), size);
                }
            }
        }

        List<String> clothesSeasons = clothesRequest.getClothesSeasons();
        if (clothesSeasons != null && !clothesSeasons.isEmpty() && clothesSeasons.size() > 0) {
            for (String season : clothesSeasons) {
                ClothesSeason finded = clothesSeasonService.findByName(clothes.getClothesID(), season);
                if (finded == null) {
                    clothesSeasonService.createSeason(clothes.getClothesID(), season);
                }
            }
        }

        List<String> clothesColors = clothesRequest.getClothesColors();
        if (clothesColors != null && !clothesColors.isEmpty() && clothesColors.size() > 0) {
            for (String color : clothesColors) {
                ClothesColor finded = clothesColorService.findByName(clothes.getClothesID(), color);
                if (finded == null) {
                    clothesColorService.createColor(clothes.getClothesID(), color);
                }
            }
        }

        List<String> clothesStyle = clothesRequest.getClothesStyle();
        if (clothesStyle != null && !clothesStyle.isEmpty() && clothesStyle.size() > 0) {
            for (String style : clothesStyle) {
                clothesStyleService.createStyle(clothes.getClothesID(), style);
            }
        }

        List<String> clothesGender = clothesRequest.getClothesGender();
        if (clothesGender != null && !clothesGender.isEmpty() && clothesGender.size() > 0) {
            for (String gender : clothesGender) {
                clothesGenderService.createGender(clothes.getClothesID(), gender);
            }
        }

        clothes = clothesRepository.getClothesByClothesID(clothes.getClothesID());
        return mapToClothesResponse(clothes);

    }

    @Override
    public void deleteClothesByID(Integer clothesID) throws CustomException {

        Clothes clothes = clothesRepository.getClothesByClothesID(clothesID);
        if (clothes == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }

        clothesColorService.deleteByClothesID(clothesID);
        clothesImageService.deleteByClothesID(clothesID);
        clothesSeasonService.deleteByClothesID(clothesID);
        clothesSizeService.deleteByClothesID(clothesID);
        clothesStyleService.deleteByClothesID(clothesID);
        clothesGenderService.deleteByClothesID(clothesID);
        clothesRepository.deleteById(clothesID);
    }

    public ClothesResponse mapToClothesResponse(Clothes clothes) throws CustomException {

        List<String> clothesImages = clothesImageService
                .getAllImageOfClothes(clothes.getClothesID())
                .stream()
                .map(clothesImage -> clothesImage.getImageUrl().toString())
                .toList();

        List<String> clothesSizes = clothesSizeService
                .getAllSizeOfClothes(clothes.getClothesID())
                .stream()
                .map(clothesSize -> clothesSize.getClothesSizeKey().getSize().name())
                .toList();

        List<String> clothesColors = clothesColorService
                .getAllColorOfClothes(clothes.getClothesID())
                .stream()
                .map(clothesColor -> clothesColor.getClothesColorKey().getColor().name())
                .toList();

        List<String> clothesSeasons = clothesSeasonService
                .getAllSeasonOfClothes(clothes.getClothesID())
                .stream()
                .map(clothesSeason -> clothesSeason.getClothesSeasonKey().getSeason().name())
                .toList();

        List<String> clothesStyles = clothesStyleService
                .getAllStyleOfClothes(clothes.getClothesID())
                .stream()
                .map(s -> s.getClothesStyleKey().getStyle().getStyleName().toString())
                .toList();

        List<String> clothesGender = clothesGenderService
                .getAllGenderOfClothes(clothes.getClothesID())
                .stream()
                .map(s -> s.getClothesGenderKey().getGenderType().toString())
                .toList();

        List<String> hashtag = new ArrayList<>();
        List<Hashtag> htl = hashtagService.getAllHashtagOfPost(clothes.getClothesID());
        if (htl != null && !htl.isEmpty() && htl.size() > 0) {
            for (Hashtag h : htl) {
                if (hashtag == null) {
                    hashtag = new ArrayList<>();
                }
                hashtag.add(h.getHashtag());
            }
        }

        List<React> reactList = reactService
                .getPostReact(clothes.getClothesID())
                .stream()
                .toList();
        List<ReactResponse> reactResponses = new ArrayList<>();
        for (React react : reactList) {
            reactResponses.add(
                    ReactResponse.builder()
                            .userID(
                                    react.getUserPostReactKey() != null ?
                                            react.getUserPostReactKey().getUserID() : null)
                            .postID(
                                    react.getUserPostReactKey() != null ?
                                            react.getUserPostReactKey().getPostID() : null)
                            .react(react.getReact() != null ?
                                    react.getReact() : null)
                            .build()
            );
        }

        List<Comments> commentsList = commentService
                .getAllByPostID(clothes.getClothesID())
                .stream()
                .toList();
        List<CommentsResponse> commentsResponses = new ArrayList<>();
        for (Comments comments : commentsList) {
            UserResponse user = userService.getUserbyUserID(comments.getUserID());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            commentsResponses.add(
                    CommentsResponse
                            .builder()
                            .commentID(comments.getCommentID())
                            .content(comments.getContent())
                            .postID(comments.getPostID())
                            .user(user)
                            .date(
                                    commentService.getDateTimeByID(comments.getCommentID())
                            )
                            .build()
            );
        }


        ClothesResponse response = ClothesResponse.builder()
                .clothesID(clothes.getClothesID())
                .nameOfProduct(clothes.getNameOfProduct())
                .typeOfClothes(clothes.getTypeOfClothes().name())
                .shape(clothes.getShape().name())
                .description(clothes.getDescription())
                .reactPerClothes(reactService.getAllReactPerClothes(clothes.getClothesID()))
                .link(clothes.getLink())
                .rating(clothes.getRating())
                .materials(clothes.getMaterials().name())
                .clothesSeasons(clothesSeasons)
                .clothesImages(clothesImages)
                .clothesSizes(clothesSizes)
                .clothesColors(clothesColors)
                .clothesStyles(clothesStyles)
                .clothesGender(clothesGender)
                .hashtag(hashtag)
                .react(reactResponses)
                .comment(commentsResponses)
                .user(userService.getUserbyUserID(clothes.getPosts().getUserID()))
                .build();
        return response;
    }

    @Override
    public List<ClothesResponse> getAllClothesByBrandID(String brandID) throws CustomException {
        List<ClothesResponse> collect = new ArrayList<>();
        for (Clothes clothes : clothesRepository.getAllClothesByBrandID(brandID)) {
            ClothesResponse clothesResponse = mapToClothesResponse(clothes);
            collect.add(clothesResponse);
        }
        return collect;
    }

    @Override
    public List<ClothesResponse> getClothesBaseOnTypeOfClothesAndColorOrMaterials(
            String typeOfClothes, String color, String materials) throws CustomException {
        List<ClothesResponse> collect = new ArrayList<>();
        for (Clothes clothes : clothesRepository.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                typeOfClothes, color,
                materials)) {
            ClothesResponse clothesResponse = mapToClothesResponse(clothes);
            collect.add(clothesResponse);
        }
        return collect;
    }

    @Override
    public List<ClothesResponse> getClothesBaseOnTypeOfClothesAndMaterial(String typeOfClothes,
                                                                          String materials) throws CustomException {
        List<ClothesResponse> collect = new ArrayList<>();
        for (Clothes clothes : clothesRepository.getClothesBaseOnTypeOfClothesAndMaterial(
                typeOfClothes, materials)) {
            ClothesResponse clothesResponse = mapToClothesResponse(clothes);
            collect.add(clothesResponse);
        }
        return collect;
    }
}
