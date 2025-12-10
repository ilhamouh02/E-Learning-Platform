document.addEventListener('DOMContentLoaded', function() {
    const timerDisplay = document.getElementById('timer');

    if (timerDisplay) {
        let timeLeft = 600; // 10 minutes en secondes

        const timerInterval = setInterval(() => {
            const minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;

            seconds = seconds < 10 ? '0' + seconds : seconds;

            timerDisplay.textContent = `${minutes}:${seconds}`;

            if (timeLeft === 0) {
                clearInterval(timerInterval);
                alert('Temps écoulé ! Le quiz va être soumis.');
                // Logique de soumission du quiz ici
            }

            timeLeft--;
        }, 1000);
    }
});
