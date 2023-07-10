function updateGenderPercentage(malePercentage, femalePercentage) {
    const maleBar = document.getElementById('malePercentage');
    const femaleBar = document.getElementById('femalePercentage');

    maleBar.style.width = malePercentage + '%';
    femaleBar.style.width = femalePercentage + '%';
}
