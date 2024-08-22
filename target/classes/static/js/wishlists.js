document.addEventListener("DOMContentLoaded", function () {
    // Handling multiple add to wishlist forms
    document.querySelectorAll('.wishlistForm').forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(this);  // Use 'this' to refer to the current form
            const filmId = this.action.split('/').pop();  // Extract filmId from the action URL
            const userId = formData.get('userId');

            const requestBody = {
                userId: Number(userId)
            };

            fetch(`/wishlists/${filmId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(requestBody)
            }).then(response => response.json())
                .then(data => {
                    alert(data.message);
                    window.location.reload();
                }).catch(error => alert('Error adding film to wishlist'));
        });
    });

    // Handling multiple delete wishlist forms
    document.querySelectorAll('.deleteWishlistForm').forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(this);  // Use 'this' to refer to the current form
            const filmId = this.action.split('/').pop();  // Extract filmId from the action URL
            const userId = formData.get('userId');

            const requestBody = {
                userId: Number(userId)
            };

            fetch(`/wishlists/${filmId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(requestBody)
            }).then(response => response.json())
                .then(data => {
                    alert(data.message);
                    window.location.reload();
                }).catch(error => alert('Error removing film from wishlist'));
        });
    });
});
