-- 회원테이블 생성
CREATE TABLE `sky`.`member` (
  `userId` VARCHAR(8) NOT NULL,
  `userPwd` VARCHAR(200) NOT NULL,
  `userName` VARCHAR(12) NULL,
  `mobile` VARCHAR(13) NULL,
  `email` VARCHAR(50) NULL,
  `registerDate` DATETIME NULL DEFAULT now(),
  `userImg` VARCHAR(50) NOT NULL DEFAULT 'avatar.png',
  `userPoint` INT NULL DEFAULT 100,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `mobile_UNIQUE` (`mobile` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);
  
  -- 계층형 게시판 생성
  CREATE TABLE `sky`.`hboard` (
  `boardNo` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(20) NOT NULL,
  `content` VARCHAR(2000) NULL,
  `writer` VARCHAR(8) NULL,
  `postDate` DATETIME NULL DEFAULT now(),
  `readCount` INT NULL DEFAULT 0,
  `ref` INT NULL DEFAULT 0,
  `step` INT NULL DEFAULT 0,
  `refOrder` INT NULL DEFAULT 0,
  PRIMARY KEY (`boardNo`),
  INDEX `hboard_member_fk_idx` (`writer` ASC) VISIBLE,
  CONSTRAINT `hboard_member_fk`
    FOREIGN KEY (`writer`)
    REFERENCES `sky`.`member` (`userId`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
COMMENT = '계층형게시판';





-- 회원 가입
insert into member(userId, userPwd, userName, mobile, email)
values(
'tosimi', 
sha1(md5('1234')), 
'토심이',
'010-2222-6666',
'tosimi@abc.com'
 );
 
 select * from member;
 
 -- 게시판에 게시글 등록
 insert into board (title, content, writer)
 values ('아싸 1등이다', '내용 무', 'tosimi');
 
  insert into board (title, content, writer)
 values ('2등임', '어휴', 'tomoong');
 
 -- 회원 가입
insert into member(userId, userPwd, userName, mobile, email)
values(
'tomoong', 
sha1(md5('1234')), 
'토뭉이',
'010-2222-8888',
'tomoong@abc.com'
 );
 
 select * from board order by boardNo desc;
 
 
insert into pointdef values ("로그인", 1);
insert into pointdef values ("글작성", 10);
insert into pointdef values ("댓글", 2);



select * from board;
select * from member;
select * from pointdef;
select * from pointlog;









insert into pointlog (pointWho, pointWhy, pointScore) values (
	'tosimi',
	'글작성',
	(select pointScore from pointdef where pointWhy = '글작성'),
);



-- 글 작성시 멤버에게 포인트 로그를 저장하는 쿼리문
select userId, userPoint from member;

set userPoint = userPoint + (select pointScore from pointdef where pointWhy = '글작성')
where userId = 'tosimi';

insert into board (title, content, writer) values ("가을입니다.", "날씨가 추워졌네요", "tomoong");
rollback;
delete from board where boardNo = 9;



insert into pointlog (pointWho, pointWhy, pointScore) values ("tomoong", "글작성", 
(select pointScore from pointdef where pointWhy = "글작성")
);
delete from pointlog where pointLogNo = 7;



insert into pointlog (pointWho, pointWhy, pointScore) values (
	"tomoong", "글작성",
	(select pointScore from pointdef where pointWhy = "글작성")
);

<insert id="insertPointlog">
	insert into pointlog (pointWho, pointWhy, pointScore) values (
		#{pointWho},boardboard
		#{pointWhy},
		10
	);
</insert>





CREATE TABLE `sky`.`boardupfiles` (
  `boardUpFileNo` INT NOT NULL AUTO_INCREMENT,
  `newFileName` VARCHAR(100) NULL,
  `originFileName` VARCHAR(100) NULL,
  `thumbFileName` VARCHAR(100) NULL,
  `ext` VARCHAR(20) NULL,
  `size` INT NULL,
  `boardNo` INT NULL,
  `base64Img` TEXT NULL,
  PRIMARY KEY (`boardUpFileNo`),
  INDEX `boardupfiles_boardNo_fk_idx` (`boardNo` ASC) VISIBLE,
  CONSTRAINT `boardupfiles_boardNo_fk`
    FOREIGN KEY (`boardNo`)
    REFERENCES `sky`.`board` (`boardNo`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
COMMENT = '게시판에 업로드되는 파일을 기록하는 테이블';

-- 게시글에 첨부한 파일정보를 저장
select max(boardNo) from board;

-- 업로드된 첨부파일을 저장하는 쿼리문
insert into boardupfiles(
	newFileName, 
    originFileName, 
    thumbFileName, 
    ext,
    size, 
    boardNo, 
    base64Img
    )
    values (
    #{newFileName},
    #{originFileName}, 
    #{thumbFileName},
    #{ext},
    #{size}, 
    #{boardNo},
    #[base64Img}, 
    )
    
-- 게시글 번호(boardNo)로 조회
select * from hboard where boardNo = 20;

-- 업로드 파일 조회
select * from boardupfiles where boardNo = 20;




select * from board;boardboard;
select * from boardupfiles;boardupfilesboardupfiles
select * from member;
select * from pointdef;
select * from pointlog;



select boardUpFileNo, newFileName, originFileName, 
thumbFileName, ext, size, boardNo, base64Img, subdir 
from boardupfiles where boardNo = 40;