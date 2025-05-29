INSERT INTO users (username, password) VALUES
('admin', '$2a$10$7F8o/6JfwxA2mrIfUZfV3.gnDWt2M4zT7fsUJ6WcUONUSbt5JBlQe');

-- Now assign roles based on username, dynamically get the user's ID
INSERT INTO user_roles (user_id, role)
SELECT id, 'ADMIN' FROM users WHERE username = 'admin';

INSERT INTO user_roles (user_id, role)
SELECT id, 'USER' FROM users WHERE username = 'admin';
