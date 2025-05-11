document.getElementById('signupForm').addEventListener('submit', function (e) {
    e.preventDefault(); // 기본 submit 막기

    const form = e.target;
    const formData = new FormData(form);

    // 체크박스는 선택된 경우에만 값이 전송되므로 명시적으로 확인
    if (!form.agree.checked) {
    alert("약관에 동의해주세요.");
    return;
    }

    fetch('/user', {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': formData.get('_csrf')
        },
        body: formData
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    alert(errorMessage);  // 여기서 브라우저 alert 발생
                    throw new Error(errorMessage);
                });
            }
            window.location.href = '/login';
        })
});

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
