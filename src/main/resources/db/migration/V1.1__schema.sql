-- V1.1__schema.sql
-- Ce script crée la structure de base avec les utilisateurs, les cours et les inscriptions.
-- Il est aligné sur le schéma XAMPP fourni et les entités JPA.

-- 1) Table des utilisateurs (users)
-- Remplace l'ancienne table `user` et gère les rôles directement.
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) DEFAULT 'STUDENT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2) Table des cours (courses)
-- Remplace l'ancienne table `course`. Le champ `teacher_id` fait référence à la table `users`.
CREATE TABLE courses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT,
  category VARCHAR(50),
  teacher_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_course_teacher FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 3) Table des inscriptions (enrollments)
-- Table de jointure pour la relation N-N entre les utilisateurs (étudiants) et les cours.
CREATE TABLE enrollments (
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  progress INT DEFAULT 0,
  completed BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (student_id, course_id),
  CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

-- Indexes pour améliorer les performances, basés sur le schéma XAMPP
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_courses_category ON courses(category);
CREATE INDEX idx_courses_teacher ON courses(teacher_id);
CREATE INDEX idx_courses_title ON courses(title);
