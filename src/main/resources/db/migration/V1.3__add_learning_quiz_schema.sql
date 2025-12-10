-- V1.3__add_learning_quiz_schema.sql
-- Ajoute les tables pour les leçons, les quiz, les questions et les tentatives,
-- en utilisant des noms de table au pluriel pour la cohérence.

-- 1) Table des leçons (lessons)
CREATE TABLE lessons (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  video_url VARCHAR(500),
  order_index INT DEFAULT 0,
  CONSTRAINT fk_lesson_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- 2) Table des quiz (quizzes)
CREATE TABLE quizzes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  lesson_id BIGINT,
  title VARCHAR(200) NOT NULL,
  time_limit INT COMMENT 'Durée en minutes',
  passing_score INT DEFAULT 70,
  CONSTRAINT fk_quiz_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

-- 3) Table des questions (questions)
CREATE TABLE questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  quiz_id BIGINT NOT NULL,
  question_text TEXT NOT NULL,
  options TEXT,
  correct_answer VARCHAR(255) NOT NULL,
  points INT DEFAULT 1,
  CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- 4) Table des tentatives de quiz (quiz_attempts)
CREATE TABLE quiz_attempts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  quiz_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  score INT,
  completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_attempt_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  CONSTRAINT fk_attempt_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes pour améliorer les performances
CREATE INDEX idx_lesson_course_order ON lessons(course_id, order_index);
CREATE INDEX idx_quiz_lesson ON quizzes(lesson_id);
CREATE INDEX idx_question_quiz ON questions(quiz_id);
CREATE INDEX idx_attempt_quiz_student ON quiz_attempts(quiz_id, student_id);
