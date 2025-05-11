document.getElementById('checkNicknameBtn').addEventListener('click', function () {
    const nickname = document.getElementById('nickname').value.trim();
    const resultText = document.getElementById('nicknameCheckResult');

    if (!nickname) {
        resultText.textContent = "닉네임을 입력해주세요.";
        resultText.style.color = "red";
        return;
    }

    fetch(`/api/nickname-check?nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(data => {
            if (data.available) {
                resultText.textContent = "사용 가능한 닉네임입니다.";
                resultText.style.color = "green";
            } else {
                resultText.textContent = "이미 사용 중인 닉네임입니다.";
                resultText.style.color = "red";
            }
        })
        .catch(error => {
            console.error("중복 확인 실패:", error);
            resultText.textContent = "확인 중 오류가 발생했습니다.";
            resultText.style.color = "red";
        });
});

function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function () {
        const output = document.getElementById('preview');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}

