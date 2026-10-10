CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','MEMBER') NOT NULL);
CREATE TABLE members (
  member_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(100),
  phone VARCHAR(20),
  joined_date DATE,
  FOREIGN KEY (user_id) REFERENCES users(user_id));
CREATE TABLE books (
  book_id INT AUTO_INCREMENT PRIMARY KEY,
  isbn VARCHAR(20) UNIQUE NOT NULL,
  title VARCHAR(200) NOT NULL,
  author VARCHAR(100) NOT NULL,
  category VARCHAR(50),
  copies_total INT DEFAULT 1,
  copies_available INT DEFAULT 1);
CREATE TABLE loans (
  loan_id INT AUTO_INCREMENT PRIMARY KEY,
  book_id INT NOT NULL,
  member_id INT NOT NULL,
  borrow_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE,
  renewals INT DEFAULT 0,
  FOREIGN KEY (book_id) REFERENCES books(book_id),
  FOREIGN KEY (member_id) REFERENCES members(member_id));
CREATE TABLE fines (
  fine_id INT AUTO_INCREMENT PRIMARY KEY,
  loan_id INT NOT NULL,
  amount DECIMAL(8,2) NOT NULL,
  paid BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (loan_id) REFERENCES loans(loan_id));
INSERT INTO books (isbn,title,author,category,copies_total,copies_available) VALUES
('9780132350884','Clean Code','Robert Martin','Programming',3,3),
('9780596009205','Head First Java','Kathy Sierra','Programming',2,2),
('9780141439518','Pride and Prejudice','Jane Austen','Fiction',2,2);
