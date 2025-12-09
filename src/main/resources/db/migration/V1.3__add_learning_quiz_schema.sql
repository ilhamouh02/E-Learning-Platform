-- V1.3__add_learning_and_quiz_tables.sql
-- Ajoute les tables nécessaires pour les leçons, quizzes, questions et tentatives
-- S'appuie sur les tables créées en V1.1 (`user`, `course`, `enrollment`)

-- 1) Table lesson
CREATE TABLE lesson (
  lessonId BIGINT AUTO_INCREMENT PRIMARY KEY,
  courseId BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT,
  videoUrl VARCHAR(1024),
  orderIndex INT DEFAULT 0,
  CONSTRAINT lesson_course_fk FOREIGN KEY (courseId) REFERENCES course(courseId) ON DELETE CASCADE,
  INDEX idx_lesson_course_order (courseId, orderIndex)
);

-- 2) Table quiz
CREATE TABLE quiz (
  quizId BIGINT AUTO_INCREMENT PRIMARY KEY,
  lessonId BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  timeLimit INT COMMENT 'minutes',
  passingScore INT DEFAULT 70,
  CONSTRAINT quiz_lesson_fk FOREIGN KEY (lessonId) REFERENCES lesson(lessonId) ON DELETE CASCADE,
  INDEX idx_quiz_lesson (lessonId)
);

-- 3) Table question
CREATE TABLE question (
  questionId BIGINT AUTO_INCREMENT PRIMARY KEY,
  quizId BIGINT NOT NULL,
  questionText TEXT NOT NULL,
  options JSON,
  correctAnswer VARCHAR(255) NOT NULL,
  points INT DEFAULT 1,
  CONSTRAINT question_quiz_fk FOREIGN KEY (quizId) REFERENCES quiz(quizId) ON DELETE CASCADE,
  INDEX idx_question_quiz (quizId)
);

-- 4) Table quiz_attempt
CREATE TABLE quiz_attempt (
  attemptId BIGINT AUTO_INCREMENT PRIMARY KEY,
  quizId BIGINT NOT NULL,
  userId BIGINT NOT NULL,
  score INT,
  completedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT quiz_attempt_quiz_fk FOREIGN KEY (quizId) REFERENCES quiz(quizId) ON DELETE CASCADE,
  CONSTRAINT quiz_attempt_user_fk FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE CASCADE,
  UNIQUE KEY unique_attempt (quizId, userId),
  INDEX idx_quiz_attempt_quiz (quizId),
  INDEX idx_quiz_attempt_user (userId)
);

-- 5) Étendre la table enrollment pour suivre la progression
ALTER TABLE enrollment
  ADD COLUMN progress INT DEFAULT 0,
  ADD COLUMN completed BOOLEAN DEFAULT FALSE;

-- Indexes supplémentaires
CREATE INDEX idx_lesson_title ON lesson(title);
CREATE INDEX idx_quiz_title ON quiz(title);
CREATE INDEX idx_question_points ON question(points);

-- Quelques données de test (optionnel pour vérification)
-- INSERT INTO lesson (courseId, title, content, videoUrl, orderIndex) VALUES (1, 'Intro', 'Contenu...', NULL, 0);
-- INSERT INTO quiz (lessonId, title, timeLimit, passingScore) VALUES (1, 'Quiz 1', 10, 70);
-- INSERT INTO question (quizId, questionText, options, correctAnswer, points) VALUES (1, 'Quelle est la capitale de la France?', JSON_ARRAY('Paris','Lyon','Marseille','Toulouse'), 'Paris', 1);
-- INSERT INTO quiz_attempt (quizId, userId, score) VALUES (1, 3, 85);
