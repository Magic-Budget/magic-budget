-- Insert 10 users with predefined static UUIDs and the same bcrypt-hashed password
INSERT INTO user_information (id, email, username, password, first_name, last_name)
VALUES
  ('123e4567-e89b-12d3-a456-426614174001', 'user1@example.com', 'user1', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'John', 'Doe1'),
  ('123e4567-e89b-12d3-a456-426614174002', 'user2@example.com', 'user2', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Jane', 'Doe2'),
  ('123e4567-e89b-12d3-a456-426614174003', 'user3@example.com', 'user3', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Alice', 'Smith'),
  ('123e4567-e89b-12d3-a456-426614174004', 'user4@example.com', 'user4', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Bob', 'Johnson'),
  ('123e4567-e89b-12d3-a456-426614174005', 'user5@example.com', 'user5', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Charlie', 'Brown'),
  ('123e4567-e89b-12d3-a456-426614174006', 'user6@example.com', 'user6', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'David', 'Williams'),
  ('123e4567-e89b-12d3-a456-426614174007', 'user7@example.com', 'user7', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Emma', 'Davis'),
  ('123e4567-e89b-12d3-a456-426614174008', 'user8@example.com', 'user8', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Frank', 'Miller'),
  ('123e4567-e89b-12d3-a456-426614174009', 'user9@example.com', 'user9', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Grace', 'Taylor'),
  ('123e4567-e89b-12d3-a456-426614174010', 'user10@example.com', 'user10', '$2a$10$tovKaIHwpHCFkFDXMY7xjOIEbFBqKRz76X6tDzkUk9BPm6RMdecV.', 'Hannah', 'Anderson');
