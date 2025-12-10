document.addEventListener('DOMContentLoaded', function() {
    const coursesContainer = document.querySelector('.row.row-cols-1.row-cols-md-3');
    const searchInput = document.querySelector('.form-control[placeholder="Rechercher un cours..."]');

    if (coursesContainer) {
        fetch('/api/courses')
            .then(response => response.json())
            .then(courses => {
                displayCourses(courses);

                if (searchInput) {
                    searchInput.addEventListener('input', function() {
                        const searchTerm = this.value.toLowerCase();
                        const filteredCourses = courses.filter(course => {
                            return course.title.toLowerCase().includes(searchTerm) ||
                                   course.description.toLowerCase().includes(searchTerm);
                        });
                        displayCourses(filteredCourses);
                    });
                }
            })
            .catch(error => {
                console.error('Erreur lors de la récupération des cours:', error);
                coursesContainer.innerHTML = '<p class="text-danger">Impossible de charger les cours.</p>';
            });
    }

    function displayCourses(courses) {
        coursesContainer.innerHTML = ''; // Vider le contenu existant
        courses.forEach(course => {
            const courseCard = `
                <div class="col">
                    <div class="card h-100 course-card">
                        <img src="https://via.placeholder.com/150" class="card-img-top" alt="Image du cours">
                        <div class="card-body">
                            <span class="badge bg-primary">${course.category}</span>
                            <h5 class="card-title mt-2">${course.title}</h5>
                            <p class="card-text">${course.description}</p>
                        </div>
                        <div class="card-footer d-flex justify-content-between align-items-center">
                            <span>👨‍🏫 ${course.teacher ? course.teacher.email : 'N/A'}</span>
                            <a href="/courses/${course.id}" class="btn btn-primary">Voir le cours</a>
                        </div>
                    </div>
                </div>
            `;
            coursesContainer.innerHTML += courseCard;
        });
    }
});
