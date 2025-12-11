-- V1.3__add_learning_and_quiz_tables.sql
-- Ajoute les tables nécessaires pour les leçons, quizzes, questions et tentatives
-- S'appuie sur les tables créées en V1.1 (`users`, `courses`, `enrollments`)

-- 1) Table lessons
CREATE TABLE lessons (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  video_url VARCHAR(500),
  order_index INT DEFAULT 0,
  course_id BIGINT NOT NULL,
  FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- 2) Table quizzes
CREATE TABLE quizzes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  time_limit INT,
  passing_score INT DEFAULT 70,
  lesson_id BIGINT,
  FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

-- 3) Table questions
CREATE TABLE questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  question_text TEXT NOT NULL,
  options JSON,
  correct_answer VARCHAR(255) NOT NULL,
  points INT DEFAULT 1,
  quiz_id BIGINT NOT NULL,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- 4) Table quiz_attempts
CREATE TABLE quiz_attempts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  score INT,
  completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  quiz_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY unique_attempt (quiz_id, student_id)
);
