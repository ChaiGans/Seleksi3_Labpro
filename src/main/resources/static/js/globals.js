document.addEventListener('DOMContentLoaded', function () {
    // Select all forms with the class 'buyForm'
    const forms = document.querySelectorAll('.buyForm');

    // Add an event listener to each form
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent the default form submission

            const filmId = this.action.split('/').pop(); // Correctly using dataset to fetch the data attribute

            // Make the fetch request
            fetch('/action/buy/' + filmId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.status === "error") {
                        alert('Purchase failed: ' + data.message);
                    } else {
                        alert('Purchase successful!');
                        window.location.reload();
                    }
                })
                .catch(error => {
                    alert('Purchase failed: Please try again.');
                });
        });
    });
});

// Utility function to get cookies by name
function getCookie(name) {
    let cookieArray = document.cookie.split(';');
    for (let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i].trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return "";
}

document.addEventListener('DOMContentLoaded', function() {
    const filmCards = document.querySelectorAll('.filmCard');
    filmCards.forEach(card => {
        card.addEventListener('click', function() {
            const filmId = this.getAttribute('data-film-id');
            window.location.href = `/details/film/${filmId}`;
        });
    });
});