$(document).ready(function () {
    const IMAGE_UPLOAD_URL = '/image/upload';
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
        toolbar: [/* 생략 */],
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
        transformTag: function (tagData) {
            if (tagData.value.length > maxTagLength) {
                alert(`태그는 최대 ${maxTagLength}자까지 입력할 수 있습니다.`);
                tagData.value = tagData.value.substring(0, maxTagLength);
            }
            return tagData;
        }
    });

    tagify.on('add', e => {
        const tagCount = tagify.value.length;
        if (tagCount > maxTags) {
            tagify.removeTag(e.detail.index);
            alert('태그는 최대 5개까지만 입력 가능');
        }
    });

    // 대표 이미지 미리보기
    // 대표 이미지 미리보기
    if (imageInput) {
        imageInput.addEventListener('change', function () {
            const file = this.files[0];
            const uploadPreviewBox = document.getElementById('imagePreviewUpload');
            const uploadPreviewImg = document.getElementById('imagePreviewImg');
            const existingPreviewBox = document.getElementById('imagePreviewExisting');

            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    if (uploadPreviewImg) {
                        uploadPreviewImg.src = e.target.result;
                        uploadPreviewBox.style.display = 'block';
                    }

                    // 기존 이미지가 있을 경우 숨기기
                    if (existingPreviewBox) {
                        existingPreviewBox.style.display = 'none';
                    }
                };
                reader.readAsDataURL(file);
            } else {
                // 이미지가 아닌 파일이거나 취소한 경우
                if (uploadPreviewImg) {
                    uploadPreviewImg.src = '';
                    uploadPreviewBox.style.display = 'none';
                }

                if (existingPreviewBox) {
                    existingPreviewBox.style.display = 'block';  // 기존 이미지 다시 보이기
                }
            }
        });
    }


    // 난이도 표시
    function toggleDifficultySection() {
        if (categorySelect && difficultySection) {
            difficultySection.style.display = categorySelect.value === "SHARE" ? "block" : "none";
        }
    }

    if (categorySelect) {
        toggleDifficultySection();
        categorySelect.addEventListener("change", toggleDifficultySection);
    }

    // 작성 / 수정 통합 처리
    $('form').on('submit', function (e) {
        e.preventDefault();

        const form = e.target;
        const formData = new FormData(form);
        const postId = $('#post-id').val();
        const title = $('#title').val().trim();
        const content = $('#summernote').summernote('code').trim();
        const category = $('#category').val();
        const csrfToken = formData.get('_csrf');

        if (!title || !content || !category) {
            alert('제목, 내용, 카테고리는 필수 입력 항목입니다.');
            return;
        }

        // 난이도 필수 체크
        if (category === 'SHARE') {
            const difficulty = $('input[name="difficulty"]:checked').val();
            if (!difficulty) {
                alert('난이도를 선택해 주세요.');
                return;
            }
        }

        formData.set('content', content);
        formData.set('tags', JSON.stringify(tagify.value.map(tag => tag.value)));

        const imageFile = document.getElementById('imageInput').files[0];
        if (imageFile) {
            formData.set('imageInput', imageFile);
        }

        const url = postId ? `/api/posts/${postId}` : '/api/posts';
        const method = postId ? 'PUT' : 'POST';

        fetch(url, {
            method: method,
            headers: {
                'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 포함
            },
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    if (postId) {
                        alert('수정이 완료되었습니다!');
                        window.location.href = `/posts/${postId}`;  // 수정된 게시글 상세 페이지로 이동
                    } else {
                        alert('게시글이 저장되었습니다!');
                        window.location.href = '/posts';  // 게시글 목록 페이지로 이동
                    }
                } else {
                    return response.json().then(err => {
                        throw new Error(err.message || '오류가 발생했습니다.');
                    });
                }
            })
            .catch(error => {
                console.error(error);
                alert('처리 실패: ' + error.message);
            });
    });
});
