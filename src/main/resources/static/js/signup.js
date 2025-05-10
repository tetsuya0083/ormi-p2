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
