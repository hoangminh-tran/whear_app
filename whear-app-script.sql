create
database whear_app;
use
whear_app;

-- USER
create table `users`
(
    `username`    nvarchar(255) primary key,
    `password`    nvarchar(255),
    `dateOfBirth` date,
    `phone`       nvarchar(255),
    `email`       nvarchar(255),
    `gender`      bit,
    `role`        nvarchar(255),
    `imgUrl`      nvarchar(255),
    `status`      bit,
    `language`    nvarchar(255)
);
create table `follow`
(
    -- `followerID` 	int auto_increment primary key,
    `followerUserID`  nvarchar(255) references `user`(`username`),
    `followingUserID` nvarchar(255) references `user`(`username`),
    primary key (followerUserID, followingUserID)
);
create table `token`
(
    `tokenID`   int auto_increment primary key,
    `expired`   date,
    `revoked`   date,
    `token`     nvarchar(255),
    `tokenType` nvarchar(255),
    `userID`    nvarchar(255) references `user`(`username`)
);

-- CUSTOMER 
create table `customer`
(
    `customerID`   nvarchar(255) primary key references `users`(`username`),
    `isFirstLogin` bit,
    `subRole`      nvarchar(255)
);
create table `history`
(
    `historyID`   int auto_increment primary key,
    `customerID`  int references `customer` (`customerID`),
    `historyItem` nvarchar(255)
);

-- BRAND
create table `brand`
(
    `brandID`     nvarchar(255) primary key references `users`(`username`),
    `description` nvarchar(255),
    `address`     nvarchar(255),
    `link`        nvarchar(255)
);
create table `news`
(
    `newsID`     int auto_increment primary key,
    `brandID`    nvarchar(255) references `brand`(`brandID`),
    `title`      nvarchar(255),
    `content`    nvarchar(255),
    `typeOfNews` nvarchar(255),
    `status`     bit
);
create table `news_image`
(
    `imgID`  int auto_increment primary key,
    `imgUrl` nvarchar(255),
    `newsID` int references `news` (`newsID`)
);

-- POST
create table `post`
(
    `postID`     int auto_increment primary key,
    `userID`     nvarchar(255) references `user`(`username`),
    `typeOfPost` nvarchar(255),
    `hashtag`    nvarchar(255),
    `date`       date,
    `status`     bit
);
create table `post_image`
(
    `imgID`    int auto_increment primary key,
    `imageUrl` nvarchar(255),
    `postID`   int references `post` (`postID`)
);

-- COLLECTION & CLOTHE
create table `collection`
(
    `collectionID`     int auto_increment primary key,
    `nameOfCollection` nvarchar(255),
    `numberOfClothes`  int,
    `typeOfCollection` nvarchar(255),
    `userID`           nvarchar(255) references `user`(`username`)
);
create table `clothes`
(
    `clothesID`     int references `post` (`postID`),
    `nameOfProduct` nvarchar(255),
    `typeOfClothes` nvarchar(255),
    `shape`         nvarchar(255),
    `seasons`       nvarchar(255),
    `description`   nvarchar(255),
    `link`          nvarchar(255),
    `rating`        int,
    `materials`     nvarchar(255)
);
create table `clothes_image`
(
    `imgID`     int auto_increment primary key,
    `imageUrl`  nvarchar(255),
    `clothesID` int references `clothes` (`clothesID`)
);
create table `collection_clothes`
(
    `collectionClothesID` int auto_increment primary key,
    `collectionID`        int references `collection` (`collectionID`),
    `clothesID`           int references `clothes` (`clothesID`)
)




