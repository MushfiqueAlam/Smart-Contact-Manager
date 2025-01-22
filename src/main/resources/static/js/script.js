console.log('Hello from script.js');
let currenttheme=getTheme();

changetheme(currenttheme);


function changetheme() {
    // Add the current theme class to the html element
    
    document.querySelector('html').classList.add(currenttheme);

    // Select the theme toggle button
    const changeThemeButton = document.querySelector("#change_theme");
    
    // Check if the button exists before adding the event listener
    if (changeThemeButton) {
        changeThemeButton.addEventListener("click", (event) => {
            console.log(currenttheme);
            // console.log(theme);
            oldtheme = currenttheme;
            // Toggle theme logic
            if (currenttheme === "light") {
               currenttheme="dark";
            } else {
                currenttheme="light";
            }
            setTheme(currenttheme);
            document.querySelector('html').classList.remove(oldtheme);
            document.querySelector('html').classList.add(currenttheme);
            changeThemeButton.querySelector('span').textContent = currenttheme==="light"?"Dark":"Light";
        });
    } else {
        console.error("Theme toggle button not found.");
    }
}

// set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme",theme);
}

// get theme from local storage
function getTheme(){
    let theme=localStorage.getItem("theme");
    return theme?theme:"light";
}