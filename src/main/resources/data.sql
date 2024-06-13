
--User 정보 자동 insert // 비밀번호 ghtjd114 암호환 것
INSERT INTO tb_user(email,name,password,phone_number,user_role)
VALUES ('winsomed6@naver.com', '한호성', '952f15428895417265636f84ae9e7dfeb2e937ee0fc24cdd8c015b4e54a701e9', '01095756300','STUDENT');

INSERT INTO tb_user(email,name,password,phone_number,user_role)
VALUES ('winsomed96@naver.com', '김포성', '952f15428895417265636f84ae9e7dfeb2e937ee0fc24cdd8c015b4e54a701e9', '01095756301','STUDENT');

INSERT INTO tb_user(email,name,password,phone_number,user_role)
VALUES ('winsomed32@naver.com', '이포성', '952f15428895417265636f84ae9e7dfeb2e937ee0fc24cdd8c015b4e54a701e9', '01095756302','PROFESSOR');

INSERT INTO tb_user(email,name,password,phone_number,user_role)
VALUES ('winsomed31@naver.com', '이구성', '952f15428895417265636f84ae9e7dfeb2e937ee0fc24cdd8c015b4e54a701e9', '01095756303','PROFESSOR');

INSERT INTO tb_user(email,name,password,phone_number,user_role)
VALUES ('winsomed33@naver.com', '이새미', '952f15428895417265636f84ae9e7dfeb2e937ee0fc24cdd8c015b4e54a701e9', '01095756304','PROFESSOR');

--강좌 정보 자동 insert
INSERT INTO tb_lecture(title, student_count, max_student_count, price)
VALUES ('강좌1', 2, 10, 10000);

INSERT INTO tb_lecture(title, student_count, max_student_count, price)
VALUES ('강좌2', 1, 10, 10000);

INSERT INTO tb_lecture(title, student_count, max_student_count, price)
VALUES ('강좌3', 3, 20, 10000);

INSERT INTO tb_lecture(title, student_count, max_student_count, price)
VALUES ('강좌4', 9, 10, 10000);

INSERT INTO tb_lecture(title, student_count, max_student_count, price)
VALUES ('강좌5', 0, 10, 10000);


