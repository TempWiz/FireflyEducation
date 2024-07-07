<?php
$videoDir = 'videos/';
$thumbnailDir = 'thumbnails/';

$videos = array_diff(scandir($videoDir), array('..', '.'));
$videoList = [];

foreach ($videos as $video) {
    $thumbnail = $thumbnailDir . pathinfo($video, PATHINFO_FILENAME) . '.jpg';
    $videoList[] = [
        'video' => $videoDir . $video,
        'thumbnail' => file_exists($thumbnail) ? $thumbnail : 'img/default-thumbnail.jpg' // Sử dụng ảnh mặc định nếu không có thumbnail
    ];
}

header('Content-Type: application/json');
echo json_encode($videoList);
?>
