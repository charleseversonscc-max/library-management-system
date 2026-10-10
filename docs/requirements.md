# Requirements - Library Management System

## User roles
- Admin / Librarian
- Member

## Functional requirements (must have)
1. Log in and log out with role-based access
2. Books: add, edit, delete, search by title, author or ISBN
3. Members: register, edit, view profile
4. Borrow and return books with an availability check
5. Due dates and a borrow limit per member
6. Automatic fines for overdue books
7. Member dashboard: borrowed books, due dates, fines
8. Admin dashboard and an overdue books report

## Nice to have
- Renewals and reservations
- Popular books report
- Member history report

## Non-functional requirements
- Passwords stored as hashes
- Input validation on all forms
- Prepared statements to prevent SQL injection
- Responsive Bootstrap pages that work on mobile
