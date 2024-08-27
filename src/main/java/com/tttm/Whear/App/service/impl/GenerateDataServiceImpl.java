package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.*;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.ClothesService;
import com.tttm.Whear.App.service.GenerateDataService;
import com.tttm.Whear.App.service.HistoryService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.ClothesRequest;
import com.tttm.Whear.App.utils.request.HistoryRequest;
import com.tttm.Whear.App.utils.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenerateDataServiceImpl implements GenerateDataService {
    private final UserService userService;

    private final ClothesService clothesService;

    private final HistoryService historyService;
    private Random random = new Random();
    private final String[] nameOfClothes = {"Áo", "Quần", "Giày", "Dép"};
    private final String[] nameBrand = {"LV", "GUCCI", "HERMES", "DIOR", "BALENCIAGA", "TOM BROWN"};

    private final String[] typeOfClothesSample = {"WIDE_LEG_PANT", "SKIRT", "PANTS", "JEANS", "JOGGER", "BOMBER", "T_SHIRT", "HOODIE", "SNEAKER", "DR_MARTENS", "CHELSEA_BOOTS"};

    private final String[] colorSample = {"BLACK", "WHITE", "GRAY", "BURNING_RED", "XANH_NEON", "YELLOW_NEON", "BROWN", "BEIGE", "BLACK", "BLUE_NAVY"};
    private final String[] materialSample = {"FLEECE", "COTTON", "POLYESTER", "DENIM", "LINEN", "FABRIC", "METAL"};
    public List<ClothesRequest> generateRandomListClothes(String userID, int size) throws CustomException
    {
        User user = userService.getUserEntityByUserID(userID);

        List<ClothesRequest> clothesRequestsList = new ArrayList<>();

        for(int i = 0; i < size; i++)
        {
            String nameOfClothes = generateRandomNameOfClothes() + " " + generateRandomNameBrand();

          //  ClothesType clothesType = getRandomEnumType(ClothesType.class);
            ClothesType clothesType = generateRandomtypeOfClothesSample();

            ShapeType shapeType = getRandomEnumType(ShapeType.class);
            SeasonType seasonsType = getRandomEnumType(SeasonType.class);

           // ClothesMaterialType materialType = getRandomEnumType(ClothesMaterialType.class);
            MaterialType materialType = generateRandomMaterial();

            List<SeasonType> seasonTypeList = generateListRandomEnumType(SeasonType.class, 2);
            List<SizeType> sizeTypeList = generateListRandomEnumType(SizeType.class, 2);

            //List<ColorType> colorTypeList = generateListRandomEnumType(ColorType.class, 2);
            List<ColorType> colorTypeList = generateRandomColorType(3);

            List<StyleType> styleTypeList = generateListRandomEnumType(StyleType.class, 2);
            ClothesRequest clothesRequest =  ClothesRequest
                    .builder()
                    .userID(user.getUserID())
                    .nameOfProduct(nameOfClothes)
                    .typeOfClothes(clothesType.name())
                    .shape(shapeType.name())
                    .materials(materialType.name())
                    .clothesSeasons(
                            seasonTypeList
                                    .stream()
                                    .map(Season -> Season.toString())
                                    .collect(Collectors.toList())
                    )
                    .clothesSizes(
                            sizeTypeList
                                    .stream()
                                    .map(Size -> Size.toString())
                                    .collect(Collectors.toList())
                    )
                    .clothesColors(
                            colorTypeList
                                    .stream()
                                    .map(Color -> Color.toString())
                                    .collect(Collectors.toList())
                    )
                    .clothesStyle(
                            styleTypeList
                                    .stream()
                                    .map(Style -> Style.toString())
                                    .collect(Collectors.toList())
                    )
                    .hashtag(
                            random.ints(1, 'A', 'Z' + 1)
                                    .mapToObj(randomChar -> random.ints(10, 'a', 'z' + 1)
                                            .mapToObj(randomLowerChar -> String.valueOf((char) randomLowerChar))
                                            .collect(Collectors.joining("", String.valueOf((char) randomChar), "")))
                                    .collect(Collectors.toList())
                    )
                    .clothesImages(
                            random.ints(1, 'A', 'Z' + 1)
                                    .mapToObj(randomChar -> random.ints(10, 'a', 'z' + 1)
                                            .mapToObj(randomLowerChar -> String.valueOf((char) randomLowerChar))
                                            .collect(Collectors.joining("", String.valueOf((char) randomChar), "")))
                                    .collect(Collectors.toList())
                    )
                    .build();
            clothesService.createClothes(clothesRequest);
            clothesRequestsList.add(clothesRequest);
        }
        return clothesRequestsList;
    }

    private Date getCurrentDate()
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    @Override
    public void generateRandomCustomer() throws CustomException {
        UserRequest userRequest = UserRequest
                .builder()
                .username("admin")
                .email("admin@gmail.com")
                .phone("01234567890")
                .password("12345")
                .language(Language.VIETNAM)
                .dateOfBirth(getCurrentDate())
                .imgUrl(generateRandomNameBrand())
                .build();

        userService.createNewUsers(userRequest);
    }

    @Override
    public  List<String> generateRandomHistoryUserSearch(String userID, int size) throws CustomException {

        User user = userService.getUserEntityByUserID(userID);

        List<String> historyUserSearch = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            historyUserSearch.add(generateRandomNameOfClothes() + " " + generateRandomNameBrand());
            historyUserSearch.add(getRandomEnumType(ClothesType.class).name());
            historyUserSearch.add(getRandomEnumType(ShapeType.class).name());
            historyUserSearch.add(getRandomEnumType(MaterialType.class).name());
        }

        List<SeasonType> seasonTypeList = generateListRandomEnumType(SeasonType.class, 2);
        List<SizeType> sizeTypeList = generateListRandomEnumType(SizeType.class, 2);
        List<ColorType> colorTypeList = generateListRandomEnumType(ColorType.class, 2);
        List<StyleType> styleTypeList = generateListRandomEnumType(StyleType.class, 2);

        for(int i = 0; i < sizeTypeList.size(); ++i)
        {
            historyUserSearch.add(seasonTypeList.get(i).name());
            historyUserSearch.add(sizeTypeList.get(i).name());
            historyUserSearch.add(colorTypeList.get(i).name());
            historyUserSearch.add(styleTypeList.get(i).name());
        }

        historyService.createHistoryItemBySearching(HistoryRequest
                .builder()
                .customerID(user.getUserID())
                .historyItems(historyUserSearch)
                .build());

        return historyUserSearch;
    }


    private String generateRandomNameOfClothes()
    {
        return nameOfClothes[random.nextInt(nameOfClothes.length)];
    }

    private String generateRandomNameBrand()
    {
        return nameBrand[random.nextInt(nameBrand.length)];
    }

    private ClothesType generateRandomtypeOfClothesSample()
    {
        return ClothesType.valueOf(typeOfClothesSample[random.nextInt(typeOfClothesSample.length)]);
    }

    private MaterialType generateRandomMaterial()
    {
        return MaterialType.valueOf(materialSample[random.nextInt(materialSample.length)]);
    }

    private List<ColorType> generateRandomColorType(int size)
    {
        List<ColorType> colors = new ArrayList<>();
        for(int i = 0; i < size; i++)
        {
            colors.add(ColorType.valueOf(colorSample[random.nextInt(colorSample.length)]));
        }
        return colors;
    }

    private <T extends Enum<?>> T getRandomEnumType(Class<T> enumType)
    {
        T[] enumValues = enumType.getEnumConstants();
        return enumValues[random.nextInt(enumValues.length)];
    }

    private <T extends Enum<?>> List<T> generateListRandomEnumType(Class<T> enumType, int size)
    {
        List<T> enumList = new ArrayList<>();

       for(int i = 0; i < size; ++i){
            T randomEnum = getRandomEnumType(enumType);
            enumList.add(randomEnum);
        }
        return enumList;
    }
}
