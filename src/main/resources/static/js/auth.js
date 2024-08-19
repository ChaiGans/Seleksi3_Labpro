document.addEventListener('DOMContentLoaded', function() {
    var logoutForm = document.getElementById('logoutForm');

    if (logoutForm) {
        logoutForm.onsubmit = function(event) {
            event.preventDefault();
            fetch(this.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => {
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
                var errorMsg = document.getElementById('errorMsg');
                if (errorMsg) {
                    errorMsg.textContent = 'Logout failed. Please try again.';
                } else {
                    console.error('Error message element not found');
                }
            });
        };
    }
});
