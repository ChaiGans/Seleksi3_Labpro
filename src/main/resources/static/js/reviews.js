document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('reviewForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = new FormData(form);
        const filmId = formData.get('filmId');
        const userId = formData.get('userId');
        const comment = formData.get('comment');
        const rating = formData.get('rating');

        console.log(rating)

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
                console.log('Success:', data);
                alert('Review submitted successfully!');
                window.location.reload();
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('Failed to submit review');
            });
    });
});
