<template>
    <div class="card">
      <div class="content">
        <div class="ui comments">
          <!-- 第一个评论 -->
          <div class="comment">
            <a class="avatar">
              <img src="../../../public/images/photo.JPG">
            </a>
            <div class="content">
              <a class="author">Joe Henderson</a>
              <div class="metadata">
                <div class="date">1 day ago</div>
              </div>
              <div class="text">
                <p>The hours, minutes and seconds stand as visible reminders that your effort put them all there. </p>
                <p>Preserve until your next run, when the watch lets you see how Impermanent your efforts are.</p>
              </div>
              <div class="actions">
                <a class="reply">Reply</a>
              </div>
            </div>
          </div>
          <!-- 第二个评论 -->
          <div class="comment">
            <a class="avatar">
              <img src="../../../public/images/photo.JPG">
            </a>
            <div class="content">
              <a class="author">Christian Rocha</a>
              <div class="metadata">
                <div class="date">2 days ago</div>
              </div>
              <div class="text">
                I re-tweeted this.
              </div>
              <div class="actions">
                <a class="reply">Reply</a>
              </div>
            </div>
          </div>
          <!-- 回复表单 -->
          <form class="ui reply form">
            <div class="field">
              <textarea></textarea>
            </div>
            <div class="ui primary submit labeled icon button">
              <i class="icon edit"></i> Add Comment
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  // 如果有需要可以添加逻辑处理
  </script>
  
  <style scoped>
  .card {
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 15px;
    margin-bottom: 20px;
  }
  
  .comment {
    margin-bottom: 15px;
  }
  
  .comment .content {
    margin-left: 80px; /* 调整评论内容与头像的距离 */
  }
  
  .comment .avatar img {
    width: 60px;
    height: 60px;
    border-radius: 50%;
  }
  </style>
  