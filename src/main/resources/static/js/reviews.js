document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('reviewForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new FormData(form);
        const filmId = formData.get('filmId');
        const userId = formData.get('userId');
        const comment = formData.get('comment');
        const rating = formData.get('rating');

        // Construct the request body
        const requestBody = {
            filmId: Number(filmId),
            userId: Number(userId),
            comment: comment,
            rating: Number(rating)
        };

        fetch(form.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(requestBody)
        })
            .then(response => response.json())
            .then(data => {
                alert('Review submitted successfully!');
                window.location.reload();
            })
            .catch((error) => {
                alert('Failed to submit review');
            });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const deleteForms = document.querySelectorAll('.review-delete-form');
    deleteForms.forEach(form => {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            const reviewId = this.querySelector('input[name="reviewId"]').value;

            fetch(`/reviews/${reviewId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
            })
                .then(response => {
                    if (response.ok) {
                        alert('Review deleted successfully');
                        window.location.reload();
                    } else {
                        alert('Failed to delete the review');
                    }
                })
                .catch(error => alert('Error deleting review:'));
        });
    });
});