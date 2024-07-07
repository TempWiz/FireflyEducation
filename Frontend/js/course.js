document.addEventListener("DOMContentLoaded", function () {
    const videoFolder = "videos/";
    const thumbnailFolder = "img/thumbnails/";
    const courseItems = document.querySelectorAll('.course-item');
    const videoList = document.getElementById("videoList");
    const selectVideoModal = new bootstrap.Modal(document.getElementById('selectVideoModal'));
    const videoModal = new bootstrap.Modal(document.getElementById('videoModal'));
    const courseVideo = document.getElementById("courseVideo");

    const sampleVideos = [
        "001 Project Intro.mp4",
        "002 My Environment & Setup.mp4",
        "003 Project Starter Boilerplate.mp4",
    ];

    courseItems.forEach(item => {
        item.addEventListener('click', function () {
            const courseId = this.getAttribute('data-course');
            loadVideos(courseId);
            selectVideoModal.show();
        });
    });

    function loadVideos(courseId) {
        videoList.innerHTML = '';
        sampleVideos.forEach(video => {
            const videoName = video.split('.')[0];
            const thumbnailSrc = `${thumbnailFolder}${videoName}.jpg`;

            const col = document.createElement("div");
            col.classList.add("col-md-4", "video-thumbnail", "mb-3");
            col.setAttribute("data-video", videoFolder + video);
            col.innerHTML = `
                <img src="${thumbnailSrc}" alt="${video} Thumbnail" onerror="this.src='img/video-placeholder.jpg'" class="img-fluid">
                <h6 class="mt-2">${video}</h6>
            `;
            col.addEventListener("click", function () {
                const videoSrc = this.getAttribute("data-video");
                courseVideo.querySelector("source").setAttribute("src", videoSrc);
                courseVideo.load();
                selectVideoModal.hide();
                videoModal.show();
            });
            videoList.appendChild(col);
        });
    }

    // Xử lý đóng modal video
    document.getElementById('videoModal').addEventListener('hidden.bs.modal', function () {
        courseVideo.pause();
        courseVideo.currentTime = 0;
        courseVideo.querySelector("source").removeAttribute("src");
    });

    // Xử lý đóng modal chọn video
    document.getElementById('selectVideoModal').addEventListener('hidden.bs.modal', function () {
        videoList.innerHTML = '';
    });

    // Thêm xử lý cho nút đóng trong các modal
    document.querySelectorAll('.modal .btn-close').forEach(button => {
        button.addEventListener('click', function () {
            const modal = this.closest('.modal');
            const modalInstance = bootstrap.Modal.getInstance(modal);
            modalInstance.hide();
        });
    });
});