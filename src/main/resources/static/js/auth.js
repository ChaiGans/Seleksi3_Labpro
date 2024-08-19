document.getElementById('logoutForm').onsubmit = function(event) {
    event.preventDefault();
    fetch(this.action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // Ensure the content type matches your backend expectations
        },
    }).then(response => {
        // Check if the response is OK (status 200-299)
        if (response.ok) {
            window.location.href = '/';
        } else {
            // Handle any errors from the server
            return response.json().then(data => {
                if (data.error) {
                    document.getElementById('errorMsg').textContent = data.message;
                } else {
                    window.location.href="/";
                }
            });
        }
    }).catch(error => {
        // Handle any network errors
        document.getElementById('errorMsg').textContent = 'Logout failed. Please try again.';
    });
};