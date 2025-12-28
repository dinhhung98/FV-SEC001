1. File output nằm trong resources/data. Trong source code này mình chỉ dùng java thuần do đề yêu cầu thao tác với file csv, nếu dùng database sẽ đơn giản hơn.
   * Do thao tác xử lý file rất tốn thời gian nên mình đã chia thành các batch để xử lý, mỗi batch sẽ chạy trên 1 thread
   * Sau khi các batch xử lý xong thì đó là lúc mình merge kết quả của các batch lại.
   * Thời gian để mình có thể xử lý xong là 6 giây. Trong source code khi runtime mình có log lại kết quả. Do app chạy java thuần nên mình ko muốn xài đến docker nữa ^^.
2. Source code đã sử dụng file data được cung cấp và để trong thư mục resources/data. Để run project cần phải:
   * Install jdk 17
   * Pull source code về.
   * Chạy file FV-SEC001.jar trong folder output với lệnh: java -jar FV-SEC001.jar
