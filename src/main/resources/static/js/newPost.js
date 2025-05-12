$(document).ready(function () {
    const IMAGE_UPLOAD_URL = '/upload-image';
    const maxTags = 5;
    const maxTagLength = 10;
    const imageInput = document.getElementById('imageInput');
    const imagePreview = document.getElementById('imagePreview');
    const tagInput = document.querySelector('#tag-input');
    const categorySelect = document.getElementById("category");
    const difficultySection = document.getElementById("difficulty-section");

    // Summernote 초기화
    $('#summernote').summernote({
        height: 300,
        lang: 'ko-KR',
        placeholder: '내용을 입력하세요',
        toolbar: [
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
            ['color', ['forecolor', 'color']],
            ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert', ['picture', 'link', 'video']],
            ['view', ['fullscreen', 'help']]
        ],
        fontNames: ['Arial', '맑은 고딕', '궁서', '굴림체'],
        fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72'],
        callbacks: {
            onImageUpload: function (files) {
                const file = files[0];
                const formData = new FormData();
                formData.append('file', file);

                $.ajax({
                    url: IMAGE_UPLOAD_URL,
                    method: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (response) {
                        $('#summernote').summernote('insertImage', response.imageUrl);
                    },
                    error: function () {
                        alert('이미지 업로드에 실패했습니다. 올바른 이미지 파일인지 확인해주세요.');
                    }
                });
            }
        }
    });

    // Tagify 초기화
    const tagify = new Tagify(tagInput, {
        delimiters: ", ",
        maxTags: maxTags,
        maxLength: 10,
        dropdown: { enabled: 0 },
        transformTag: function(tagData) {
            if (tagData.value.length > maxTagLength) {
                alert(`태그는 최대 ${maxTagLength}자까지 입력할 수 있습니다.`);
                tagData.value = tagData.value.substring(0, maxTagLength);
            }
            return tagData;
        }
    });

    tagify.on('add', e => {
        const currentTags = tagify.value.map(tag => tag.value);
        const tagCount = currentTags.length;

        // 태그 개수 체크
        if (tagCount > maxTags) {
            tagify.removeTag(e.detail.index);
            alert('태그는 최대 5개까지만 입력 가능');
            return;
        }

        // 태그 길이 체크
        if (e.detail.data.value.length > maxTagLength) {
            tagify.removeTag(e.detail.index);
            alert(`태그는 최대 ${maxTagLength}자까지만 입력 가능`);
            return;
        }
    });

    // 대표 이미지 미리보기
    if (imageInput && imagePreview) {
        imageInput.addEventListener('change', function () {
            const file = this.files[0];

            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    imagePreview.innerHTML = `<img src="${e.target.result}" alt="대표 이미지 미리보기">`;
                    imagePreview.classList.add('has-image');
                };
                reader.readAsDataURL(file);
            } else {
                imagePreview.innerHTML = '';
                imagePreview.classList.remove('has-image');
            }
        });
    }

    // 난이도 섹션 표시 제어
    function toggleDifficultySection() {
        if (categorySelect && difficultySection) {
            const selectedCategory = categorySelect.value;
            if (selectedCategory === "SHARE") {
                difficultySection.style.display = "block";
            } else {
                difficultySection.style.display = "none";
            }
        }
    }

    if (categorySelect) {
        toggleDifficultySection();
        categorySelect.addEventListener("change", toggleDifficultySection);
    }

    // 폼 제출 처리
    $('form').on('submit', function (e) {
        e.preventDefault();

        const form = e.target;
        const formData = new FormData(form);

        const title = $('#title').val().trim();
        const content = $('#summernote').summernote('code').trim();
        const category = $('#category').val();

        // 필수 입력 항목 체크
        if (!title || !content || !category) {
            alert('제목, 내용, 카테고리는 필수 입력 항목입니다.');
            return;
        }

        // SHARE 카테고리일 때 난이도 체크
        if (category === "SHARE") {
            const selectedDifficulty = $('input[name="difficulty"]:checked').val();
            if (!selectedDifficulty) {
                alert('난이도를 선택해 주세요.');
                return;
            }
            formData.append('difficulty', selectedDifficulty.toUpperCase());
        } else {
            formData.delete('difficulty');
        }


        fetch('/api/posts', {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': formData.get('_csrf') // CSRF 토큰을 헤더에 포함
            },
            body: formData // formData를 body로 전송
        })
            .then(response => {
                if (response.ok) {
                    alert('게시글이 저장되었습니다!'); // 성공 시 메시지 출력
                    window.location.href = '/posts'; // 게시글 목록 페이지로 리디렉션
                } else {
                    return response.json().then(err => {
                        throw new Error(err.message || '저장 중 오류가 발생했습니다.'); // 오류 메시지 처리
                    });
                }
            })
            .catch(error => {
                console.error(error);
                alert('저장 실패: ' + error.message); // 오류 메시지 출력
            });
    });
});