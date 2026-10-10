USE library_db;
INSERT INTO users (username, password_hash, role) VALUES
('admin','CHANGE_ME','ADMIN'),
('member1','CHANGE_ME','MEMBER'),
('member2','CHANGE_ME','MEMBER');
INSERT INTO members (user_id, full_name, email, phone, joined_date) VALUES
(2,'Sample Member One','member1@example.com','0900000001','2026-10-10'),
(3,'Sample Member Two','member2@example.com','0900000002','2026-10-10');
INSERT INTO books (isbn,title,author,category,copies_total,copies_available) VALUES
('9780134685991','Effective Java','Joshua Bloch','Programming',2,2),
('9780201633610','Design Patterns','Erich Gamma','Programming',2,2),
('9780135957059','The Pragmatic Programmer','David Thomas','Programming',2,2),
('9780262046305','Introduction to Algorithms','Thomas Cormen','Programming',1,1),
('9780451524935','1984','George Orwell','Fiction',3,3),
('9780061120084','To Kill a Mockingbird','Harper Lee','Fiction',2,2),
('9780743273565','The Great Gatsby','F. Scott Fitzgerald','Fiction',2,2);
