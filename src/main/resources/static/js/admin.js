console.log("Admin ");
document.querySelector("#image_file_input").addEventListener('change', function(event) {
    const file = event.target.files[0];

    // Check if a file is selected
    if (file) {
        // Validate file size
        if (file.size > 5 * 1024 * 1024) { // 5MB limit
            alert('File size must be less than 5MB');
            this.value = ''; // Clear the input
            document.getElementById("upload_image_preview").src = ''; // Clear the preview
            return; // Exit the function
        }

        // Validate file type (optional)
        const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
        if (!validTypes.includes(file.type)) {
            alert('Please upload a valid image file (JPEG, PNG, GIF).');
            this.value = ''; // Clear the input
            document.getElementById("upload_image_preview").src = ''; // Clear the preview
            return; // Exit the function
        }

        // Preview the image
        const reader = new FileReader();
        reader.onload = function() {
            document.getElementById("upload_image_preview").src = reader.result;
        }
        reader.readAsDataURL(file);
    }
});