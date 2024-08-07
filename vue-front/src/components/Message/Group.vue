<template>
  <div class="container">
<<<<<<< HEAD
      <h4>建立群組</h4>
      <div class="create-group">
          <input v-model="newGroupName" placeholder="群組名稱" />
          <select v-model="selectedStatus">
              <option value="PUBLIC">公開</option>
              <option value="PRIVATE">私密</option>
          </select>
          <input type="file" @change="handleFileUpload" />
          <button @click="createGroup">建立</button>
          <p v-if="createGroupMessage">{{ createGroupMessage }}</p>

      </div>
      
      <h4>搜尋群組</h4>
      <div class="search-group">
          <input v-model="searchQuery" placeholder="輸入群組名稱" />
          <button @click="searchGroup">搜尋</button>
      </div>
      
      <div class="search-results" v-if="searchResults.length">
          <div class="group-box" v-for="(group, index) in searchResults" :key="index">
              <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
              <div v-else class="group-no-image">無圖片</div>
              <div class="group-info">
                  <div class="group-name">{{ group.name }}</div>
                  <div class="group-status">{{ group.status }}</div>
                  <button @click="joinGroup(group.id)">加入</button>
              </div>
              <!-- 彈出 加入 成員 名稱 圖片 -->
                <div v-if="isMemberVisible" class="member-card">
                  <p>成員名稱和圖片</p>
                  <input v-model="newMemberName" placeholder="成員名稱" />
                  <input type="file" @change="handleMemberFileUpload" />
                  <button @click="addMemberNameAndImage(group.id)">建立</button>

                </div>
          </div>
      </div>

      

      <div class="my-groups">
          <h4>我的群組</h4>

          <p @click="toggleVisibility('public')" class="group-category">
              {{ isPublicVisible ? '−' : '+' }} 公開 ({{ publicGroups.length }})
          </p>
          <ul v-if="isPublicVisible">
              <li v-for="(group, index) in publicGroups" :key="index" class="group-item">
                  <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
                  <div v-else class="group-no-image">無圖片</div>
                  <div class="group-info">
                      {{ group.groupName }}
                      <button @click="connect(group.groupId)">進入</button>
                      <button @click="leavegroup(group.groupId)">離開群組</button>
                      <button v-if="group.isAdmin" @click="manageGroup(group.groupId)">管理群組</button> <!-- 新增管理群組按鈕 -->
                  </div>
              </li>
          </ul>

          <p @click="toggleVisibility('private')" class="group-category">
              {{ isPrivateVisible ? '−' : '+' }} 私密 ({{ privateGroups.length }})
          </p>
          <ul v-if="isPrivateVisible">
              <li v-for="(group, index) in privateGroups" :key="index" class="group-item">
                  <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
                  <div v-else class="group-no-image">無圖片</div>
                  <div class="group-info">
                      {{ group.groupName }}
                      <button @click="connect(group.groupId)">進入</button>
                      <button @click="leavegroup(group.groupId)">離開群組</button>
                      <button v-if="group.isAdmin" @click="readyallow(group.groupId)">成員審核</button>
                      <button v-if="group.isAdmin" @click="manageGroup(group.groupId)">管理群組</button>
                  </div>
              </li>
          </ul>

          <p @click="toggleVisibility('friends')" class="group-category">
              {{ isFriendsVisible ? '−' : '+' }} 朋友 ({{ friendsGroups.length }})
          </p>
          <ul v-if="isFriendsVisible">
              <li v-for="(group, index) in friendsGroups" :key="index" class="group-item">
                  <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
                  <div v-else class="group-no-image">無圖片</div>
                  <div class="group-info">
                      {{ group.groupName }}
                      <button @click="connect(group.groupId)">進入</button>
                      <button @click="leavegroup(group.groupId)">離開群組</button>
                  </div>
              </li>
          </ul>
      </div>

      <!-- 彈出聊天窗口 -->
      <div v-if="isChatRoomVisible" class="chat-room-popup">
    <div class="chat-room-header">
      <h3>聊天室</h3>
      <button class="close-button" @click="toggleChatRoom">X</button>
    </div>
    <div id="chat" class="chat-box">
      <div v-for="msg in chat" :key="msg.id" :class="['chat-message', { 'self': msg.sender_id === sessionId }]">
        <div class="member-info">
          <img v-if="msg.memberImage && msg.sender_id !== sessionId" :src="msg.memberImage" alt="Member Image" class="member-image" />
          <div v-if="msg.memberName && msg.sender_id !== sessionId" class="member-name">{{ msg.memberName }}</div>
        </div>
        <div v-if="msg.content">{{ msg.content }}</div>
        <img v-if="msg.image" :src="msg.image" alt="Image" class="chat-image" />
      </div>
    </div>
    <div class="chat-inputs">
      <input v-model="messageInput" type="text" placeholder="輸入消息..." />
      <input ref="imageInput" type="file" />
      <button @click="sendMessage">發送</button>
    </div>
  </div>

    <div v-if="isManageVisible" id="sidebar" class="manage-group">
  <div class="manage-header">
    <h4>管理群組</h4>
    <button class="close-button" @click="isManageVisible = false">X</button>
  </div>
  <ul class="member-list">
    <li v-for="(member, index) in manageGroups" :key="index" class="member-card">
      <div class="member-info">
        <img :src="'data:image/png;base64,' + member.image" alt="Member Image" class="member-image" />
        <div class="member-details">
          <!-- <div>Member ID: {{ member.memberId }}</div>
          <div>User ID: {{ member.userId }}</div>
          <div>Is Check: {{ member.isCheck }}</div> -->
          <div>Name: {{ member.name }}</div>
        </div>
      </div>
      <div class="member-actions">
        <button v-if="member.isCheck === true" @click="approveGroup(member)">審核</button>
        <button v-if="member.isCheck === false" @click="removeMemeber(member)">移除成員</button>
      </div>
    </li>
  </ul>
</div>
=======
    <h4>建立群組</h4>
    <div class="create-group">
      <input v-model="newGroupName" placeholder="群組名稱" />
      <select v-model="selectedStatus">
        <option value="PUBLIC">公開</option>
        <option value="PRIVATE">私密</option>
      </select>
      <input type="file" @change="handleFileUpload" />
      <button @click="createGroup">建立</button>
      <p v-if="createGroupMessage">{{ createGroupMessage }}</p>

    </div>

    <h4>搜尋群組</h4>
    <div class="search-group">
      <input v-model="searchQuery" placeholder="輸入群組名稱" />
      <button @click="searchGroup">搜尋</button>
    </div>

    <div class="search-results" v-if="searchResults.length">
      <div class="group-box" v-for="(group, index) in searchResults" :key="index">
        <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
        <div v-else class="group-no-image">無圖片</div>
        <div class="group-info">
          <div class="group-name">{{ group.name }}</div>
          <div class="group-status">{{ group.status }}</div>
          <button @click="joinGroup(group.id)">加入</button>
        </div>
        <!-- 彈出 加入 成員 名稱 圖片 -->
        <div v-if="isMemberVisible" class="member-card">
          <p>成員名稱和圖片</p>
          <input v-model="newMemberName" placeholder="成員名稱" />
          <input type="file" @change="handleMemberFileUpload" />
          <button @click="addMemberNameAndImage(group.id)">建立</button>

        </div>
      </div>
    </div>



    <div class="my-groups">
      <h4>我的群組</h4>

      <p @click="toggleVisibility('public')" class="group-category">
        {{ isPublicVisible ? '−' : '+' }} 公開 ({{ publicGroups.length }})
      </p>
      <ul v-if="isPublicVisible">
        <li v-for="(group, index) in publicGroups" :key="index" class="group-item">
          <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
          <div v-else class="group-no-image">無圖片</div>
          <div class="group-info">
            {{ group.groupName }}
            <button @click="connect(group.groupId)">進入</button>
            <button @click="leavegroup(group.groupId)">離開群組</button>
            <button v-if="group.isAdmin" @click="manageGroup(group.groupId)">管理群組</button> <!-- 新增管理群組按鈕 -->
          </div>
        </li>
      </ul>

      <p @click="toggleVisibility('private')" class="group-category">
        {{ isPrivateVisible ? '−' : '+' }} 私密 ({{ privateGroups.length }})
      </p>
      <ul v-if="isPrivateVisible">
        <li v-for="(group, index) in privateGroups" :key="index" class="group-item">
          <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
          <div v-else class="group-no-image">無圖片</div>
          <div class="group-info">
            {{ group.groupName }}
            <button @click="connect(group.groupId)">進入</button>
            <button @click="leavegroup(group.groupId)">離開群組</button>
            <button v-if="group.isAdmin" @click="readyallow(group.groupId)">成員審核</button>
            <button v-if="group.isAdmin" @click="manageGroup(group.groupId)">管理群組</button>
          </div>
        </li>
      </ul>

      <p @click="toggleVisibility('friends')" class="group-category">
        {{ isFriendsVisible ? '−' : '+' }} 朋友 ({{ friendsGroups.length }})
      </p>
      <ul v-if="isFriendsVisible">
        <li v-for="(group, index) in friendsGroups" :key="index" class="group-item">
          <img v-if="group.image" :src="'data:image/png;base64,' + group.image" alt="Group Image" class="group-image" />
          <div v-else class="group-no-image">無圖片</div>
          <div class="group-info">
            {{ group.groupName }}
            <button @click="connect(group.groupId)">進入</button>
            <button @click="leavegroup(group.groupId)">離開群組</button>
          </div>
        </li>
      </ul>
    </div>

    <!-- 彈出聊天窗口 -->
    <div v-if="isChatRoomVisible" class="chat-room-popup">
      <div class="chat-room-header">
        <h3>聊天室</h3>
        <button class="close-button" @click="toggleChatRoom">X</button>
      </div>
      <div id="chat" class="chat-box">
        <div v-for="msg in chat" :key="msg.id" :class="['chat-message', { 'self': msg.sender_id === sessionId }]">
          <div class="member-info">
            <img v-if="msg.memberImage && msg.sender_id !== sessionId" :src="msg.memberImage" alt="Member Image"
              class="member-image" />
            <div v-if="msg.memberName && msg.sender_id !== sessionId" class="member-name">{{ msg.memberName }}</div>
          </div>
          <div v-if="msg.content">{{ msg.content }}</div>
          <img v-if="msg.image" :src="msg.image" alt="Image" class="chat-image" />
        </div>
      </div>
      <div class="chat-inputs">
        <input v-model="messageInput" type="text" placeholder="輸入消息..." />
        <input ref="imageInput" type="file" />
        <button @click="sendMessage">發送</button>
      </div>
    </div>

    <div v-if="isManageVisible" id="sidebar" class="manage-group">
      <div class="manage-header">
        <h4>管理群組</h4>
        <button class="close-button" @click="isManageVisible = false">X</button>
      </div>
      <ul class="member-list">
        <li v-for="(member, index) in manageGroups" :key="index" class="member-card">
          <div class="member-info">
            <img :src="'data:image/png;base64,' + member.image" alt="Member Image" class="member-image" />
            <div class="member-details">
              <!-- <div>Member ID: {{ member.memberId }}</div>
          <div>User ID: {{ member.userId }}</div>
          <div>Is Check: {{ member.isCheck }}</div> -->
              <div>Name: {{ member.name }}</div>
            </div>
          </div>
          <div class="member-actions">
            <button v-if="member.isCheck === true" @click="approveGroup(member)">審核</button>
            <button v-if="member.isCheck === false" @click="removeMemeber(member)">移除成員</button>
          </div>
        </li>
      </ul>
    </div>
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764

  </div>
</template>

    
<script setup>
import { ref, onMounted } from 'vue';
import axiosapi from '@/plugins/axios';
import Swal from 'sweetalert2';
import { nextTick } from 'vue';

// 定義組件的props
const sessionId = ref(sessionStorage.getItem("Session-Id"));
const createGroupMessage = ref("");
const newGroupName = ref("");
const selectedStatus = ref("PRIVATE");
const searchQuery = ref("");
const searchResults = ref([]);
// 成員名稱和圖片
const isMemberVisible = ref(false);
const newMemberName = ref("");
const isPublicVisible = ref(false);
const isPrivateVisible = ref(false);
const isFriendsVisible = ref(false);
const imageBase64 = ref("");

const isChatRoomVisible = ref(false);
const messageInput = ref('');
const chat = ref([]);
const imageInput = ref(null);
const ws = ref(null);

const publicGroups = ref([]);
const privateGroups = ref([]);
const friendsGroups = ref([]);
const isManageVisible = ref(false);
const manageGroups = ref([]);


const toggleVisibility = (type) => {
  if (type === 'public') {
    isPublicVisible.value = !isPublicVisible.value;
  } else if (type === 'private') {
    isPrivateVisible.value = !isPrivateVisible.value;
  } else if (type === 'friends') {
    isFriendsVisible.value = !isFriendsVisible.value;
  }
};

function handleFileUpload(event) {
  const file = event.target.files[0];
  if (file.size > 2 * 1024 * 1024) {
    Swal.fire({
      icon: "error",
      text: "圖片大小不能超過2MB",
    });
    return;
  }

  const reader = new FileReader();
  reader.onload = (e) => {
    imageBase64.value = e.target.result.split(',')[1];
  };
  reader.readAsDataURL(file);
}

function handleMemberFileUpload(event) {
  const file = event.target.files[0];
  if (file.size > 2 * 1024 * 1024) {
    Swal.fire({
      icon: "error",
      text: "圖片大小不能超過2MB",
    });
    return;
  }
  const reader = new FileReader();
  reader.onload = (e) => {
    imageBase64.value = e.target.result.split(',')[1];
  };
  reader.readAsDataURL(file);
};

function createGroup() {
  if (!imageBase64.value) {
    Swal.fire({
      icon: "error",
      text: "請選擇一張圖片",
    });
    return;
  }

  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  Swal.fire({
    text: "執行中......",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post(`/ajax/pages/Group/add`, { name: newGroupName.value, status: selectedStatus.value, image: imageBase64.value })
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
        createGroupMessage.value = response.data.message;
        newGroupName.value = "";
        selectedStatus.value = "PUBLIC";
        imageBase64.value = "";
        loadMyGroups();
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "新增錯誤：" + error.message,
      });
    });
}

// 找是否為管理者
function checkIfAdmin(groupId) {
  return new Promise((resolve, reject) => {
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.get(`/ajax/pages/Group/isAdmin/${groupId}`)
      .then(response => {
        if (response.data.success) {
          resolve(response.data.isAdmin); // 假設 API 返回的是 { success: true, isAdmin: true/false }
        } else {
          Swal.fire({
            icon: "info",
            text: response.data.message,
          });
          resolve(false); // 默認情況下非管理者
        }
      })
      .catch(error => {
        Swal.fire({
          icon: "error",
          text: "錯誤：" + error.message,
        });
        reject(error);
      });
  });
}

function addMemberNameAndImage(groupId) {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  Swal.fire({
    text: "執行中......",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

<<<<<<< HEAD
  axiosapi.post(`/ajax/pages/Group/memberNameAndImage`, { id: groupId, name: newMemberName.value , image: imageBase64.value })
=======
  axiosapi.post(`/ajax/pages/Group/memberNameAndImage`, { id: groupId, name: newMemberName.value, image: imageBase64.value })
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "新增錯誤：" + error.message,
      });
    });
}

function searchGroup() {
  Swal.fire({
    text: "執行中......",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post(`/ajax/pages/Group/search`, { name: searchQuery.value })
    .then(response => {
      if (response.data.success) {
        searchResults.value = response.data.list;
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
      } else {
        searchResults.value = [];
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      if (error.response && error.response.status === 500) {
        Swal.fire({
          icon: "error",
          text: "伺服器錯誤，請稍後再試。",
        });
      } else {
        Swal.fire({
          icon: "error",
          text: "搜尋錯誤：" + error.message,
        });
      }
    });
}

function loadMyGroups() {
  Swal.fire({
    text: "加載中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post(`/ajax/pages/Group/myGroup`)
    .then(response => {
      if (response.data.success) {
<<<<<<< HEAD
        const groups = response.data.message.allowList;

        // 使用 Promise.all 等待所有管理者檢查完成
        Promise.all(groups.map(group => 
=======
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });

        const groups = response.data.message.allowList;
        if (groups.length === 0) {
          Swal.fire({
            icon: "info",
            text: "目前沒有任何群組。",
          });
          return;
        }

        // 使用 Promise.all 等待所有管理者檢查完成
        Promise.all(groups.map(group =>
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
          checkIfAdmin(group.groupId).then(isAdmin => {
            group.isAdmin = isAdmin;
          })
        ))
<<<<<<< HEAD
        .then(() => {
          publicGroups.value = groups.filter(group => group.status === 'PUBLIC');
          privateGroups.value = groups.filter(group => group.status === 'PRIVATE');
          friendsGroups.value = groups.filter(group => group.status === 'FRIENDS');
          Swal.close();
        });
=======
          .then(() => {
            publicGroups.value = groups.filter(group => group.status === 'PUBLIC');
            privateGroups.value = groups.filter(group => group.status === 'PRIVATE');
            friendsGroups.value = groups.filter(group => group.status === 'FRIENDS');
            Swal.close();

          });
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "加載錯誤：" + error.message,
      });
    });
}

function joinGroup(groupId) {
  Swal.fire({
    text: "加入群組中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });
  isMemberVisible.value = true;
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post(`/ajax/pages/Gmembers/add`, { id: groupId })
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
        loadMyGroups();
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
          showCancelButton: true,
          confirmButtonText: '申請',
          cancelButtonText: '關閉',
        }).then(result => {
<<<<<<< HEAD
            if(result.isConfirmed){
                // 用戶點申請
                console.log(groupId);
                axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
                axiosapi.post(`/ajax/pages/private/check`, { id: groupId})
                .then(response => {
                    if (response.data.success){
                        Swal.fire({
                            icon: "success",
                            text: response.data.message,
                        });
                    }else {
                        Swal.fire({
                            icon: "info",
                            text: response.data.message,
                        });
                        }
                })
            }
        })
        
=======
          if (result.isConfirmed) {
            // 用戶點申請
            console.log(groupId);
            axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
            axiosapi.post(`/ajax/pages/private/check`, { id: groupId })
              .then(response => {
                if (response.data.success) {
                  Swal.fire({
                    icon: "success",
                    text: response.data.message,
                  });
                } else {
                  Swal.fire({
                    icon: "info",
                    text: response.data.message,
                  });
                }
              })
          }
        })

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "加入錯誤：" + error.message,
      });
    });
}

// 驗證是否為有效的 JSON
function isValidJson(jsonString) {
  try {
    JSON.parse(jsonString);
    return true;
  } catch (e) {
    return false;
  }
}

const toggleChatRoom = () => {
  if (isChatRoomVisible.value) {
    // 當聊天室顯示時，斷開 WebSocket 連接
    disconnect();
  }
  isChatRoomVisible.value = !isChatRoomVisible.value;
};

// 滾動到聊天內容的最底部
const scrollToBottom = () => {
  nextTick(() => {
    const chatBox = document.getElementById('chat');
    if (chatBox) {
      chatBox.scrollTop = chatBox.scrollHeight;
    }
  });
};

// 連接 WebSocket
function connect(groupId) {
  const sessionId = sessionStorage.getItem("Session-Id");
  if (!sessionId) {
    alert('Session-Id 不存在');
    return;
  }

<<<<<<< HEAD
  ws.value = new WebSocket(`ws://192.168.23.82:8080/chat?groupId=${groupId}&session-id=${sessionId}`);
=======
  ws.value = new WebSocket(`ws://192.168.23.137:8080/chat?groupId=${groupId}&session-id=${sessionId}`);
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764

  ws.value.onopen = () => {
    isChatRoomVisible.value = true;
    // 連接成功後，獲取先前的消息
    axiosapi.post(`/ajax/pages/Message/findtmesges`, { groupId })
      .then(response => {
        chat.value = response.data.messages.map(msg => ({
          ...msg,
          image: msg.image ? 'data:image/png;base64,' + msg.image : null,
<<<<<<< HEAD
          memberImage: msg.memberImage ? 'data:image/png;base64,'+ msg.memberImage : null
=======
          memberImage: msg.memberImage ? 'data:image/png;base64,' + msg.memberImage : null
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
        }));
        scrollToBottom(); // 加载完消息后滚动到底部
      })
      .catch(error => console.error('Error fetching messages:', error));
  };

  ws.value.onmessage = event => {
    if (isValidJson(event.data)) {
      const data = JSON.parse(event.data);
      chat.value.push({
        ...data,
        image: data.image ? 'data:image/png;base64,' + data.image : null,
<<<<<<< HEAD
        memberImage: data.memberImage ? 'data:image/png;base64,'+ data.memberImage : null
=======
        memberImage: data.memberImage ? 'data:image/png;base64,' + data.memberImage : null
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
      });
      scrollToBottom(); // 接收到新消息后滚动到底部
    } else {
      console.error("Error parsing JSON: ", event.data);
    }
  };

  ws.value.onclose = () => {
<<<<<<< HEAD
  console.log("WebSocket closed.");
  ws.value = null;
  isChatRoomVisible.value = false;
};
=======
    console.log("WebSocket closed.");
    ws.value = null;
    isChatRoomVisible.value = false;
  };
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

// 斷開 WebSocket 連線
function disconnect() {
  if (ws.value) {
    ws.value.close();
  }
}

// 發送消息
function sendMessage() {
  if (!ws.value || ws.value.readyState !== WebSocket.OPEN) {
    console.error("WebSocket connection is not open.");
    return;
  }

  const groupId = new URL(ws.value.url).searchParams.get('groupId'); // 從 WebSocket URL 獲取 groupId
  const file = imageInput.value.files[0];

  if (!messageInput.value && !file) {
    Swal.fire({
      icon: "warning",
      text: "消息內容不能為空。",
    });
    return;
  }

<<<<<<< HEAD
  let  chatMessage = {
=======
  let chatMessage = {
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
    groupId: groupId,
    content: messageInput.value || "", // 確保 content 至少是空字串
    sender_id: sessionStorage.getItem("Session-Id") || "default-id" // 使用 Session-Id
  };

  if (file) {
    const reader = new FileReader();
<<<<<<< HEAD
    reader.onload = function(event) {
      chatMessage.image = event.target.result.split(',')[1]; // 取得 Base64 字符串
      axiosapi.post(`/ajax/pages/Message/tmesges`, chatMessage)
=======
    reader.onload = function (event) {
      chatMessage.image = event.target.result.split(',')[1]; // 取得 Base64 字符串
      axiosapi.post(`ajax/pages/Message/tmesges`, chatMessage)
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
        .then(response => {
          if (response.data.success) {
            chatMessage = {
              ...chatMessage,
              memberName: response.data.success.memberName,
              memberImage: response.data.success.memberImage
            };
            ws.value.send(JSON.stringify(chatMessage));
            messageInput.value = ''; // 清空輸入框
            imageInput.value.value = ''; // 清空文件選擇框
          } else {
            console.error('Error sending message:', response.data.message);
          }
        })
        .catch(error => console.error('Error sending message:', error));
    };
    reader.readAsDataURL(file);
  } else {
    chatMessage.image = ""; // 沒有圖片時，確保 image 是空字串
    axiosapi.post(`/ajax/pages/Message/tmesges`, chatMessage)
      .then(response => {
        if (response.data.success) {
          chatMessage = {
<<<<<<< HEAD
              ...chatMessage,
              memberName: response.data.success.memberName,
              memberImage: response.data.success.memberImage
            };
=======
            ...chatMessage,
            memberName: response.data.success.memberName,
            memberImage: response.data.success.memberImage
          };
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
          ws.value.send(JSON.stringify(chatMessage));
          messageInput.value = ''; // 清空輸入框
        } else {
          console.error('Error sending message:', response.data.message);
        }
      })
      .catch(error => console.error('Error sending message:', error));
  }
}

<<<<<<< HEAD
function leavegroup(groupId){
    Swal.fire({
=======
function leavegroup(groupId) {
  Swal.fire({
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
    text: "離開群組中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });
<<<<<<< HEAD
    console.log(groupId);
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.delete(`/ajax/pages/Gmembers/leave/`+groupId)
    .then(response => {
        if (response.data.success) {
        Swal.fire({
            icon: "success",
            text: response.data.message,
        });
        loadMyGroups();
        }else{
        Swal.fire({
            icon: "warning",
            text: response.data.message,
        });
        }
=======
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.delete(`/ajax/pages/Gmembers/leave/` + groupId)
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: response.data.message,
        });
        loadMyGroups(response.data.message);
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message,
        });
      }
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
    }).catch(error => {
      Swal.fire({
        icon: "error",
        text: "離開錯誤：" + error.message,
      });
    }

    );
}

// /Group/manage
function manageGroup(groupId) {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
<<<<<<< HEAD
  axiosapi.get(`/ajax/pages/Group/myGroup/memberlist/`+groupId)
    .then(response => {
      if (response.data.success) {
                isManageVisible.value = true;

                // 解析回傳的 JSON 並設置 manageGroups
                const memberlist = JSON.parse(response.data.message).memberlist;
                manageGroups.value = memberlist;
            } else {
                Swal.fire({
                    icon: "info",
                    text: response.data.message,
                });
            }
        })
        .catch(error => {
            Swal.fire({
                icon: "error",
                text: "管理群組錯誤：" + error.message,
            });
        });
=======
  axiosapi.get(`/ajax/pages/Group/myGroup/memberlist/` + groupId)
    .then(response => {
      if (response.data.success) {
        isManageVisible.value = true;

        // 解析回傳的 JSON 並設置 manageGroups
        const memberlist = JSON.parse(response.data.message).memberlist;
        manageGroups.value = memberlist;
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "管理群組錯誤：" + error.message,
      });
    });
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

// /Group/readyallow
function readyallow(groupId) {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post(`/ajax/pages/Grtes/ttst2`, { id: groupId })
    .then(response => {
      if (response.data.success) {
<<<<<<< HEAD
                isManageVisible.value = true;

                // 解析回傳的 JSON 並設置 manageGroups
                const allowList = JSON.parse(response.data.message).allowList;
                manageGroups.value = allowList;
            } else {
                Swal.fire({
                    icon: "info",
                    text: response.data.message,
                });
            }
        })
        .catch(error => {
            Swal.fire({
                icon: "error",
                text: "管理群組錯誤：" + error.message,
            });
        });
=======
        isManageVisible.value = true;

        // 解析回傳的 JSON 並設置 manageGroups
        const allowList = JSON.parse(response.data.message).allowList;
        manageGroups.value = allowList;
      } else {
        Swal.fire({
          icon: "info",
          text: response.data.message,
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "管理群組錯誤：" + error.message,
      });
    });
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

function approveGroup(member) {
  // 這裡的 body 是需要提交的數據
  const requestBody = {
    allowList: [
      {
        isCheck: false, // 可以根據實際情況修改這個值
        userId: member.userId, // 替換為實際的 userId
        memberId: member.memberId, // 或者其他合適的值
        groupId: member.groupId
      }
      // 如果有多個項目，可以在這裡添加更多
    ]
  };
  // console.log(requestBody);
  Swal.fire({
    text: "正在審核中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });

  axiosapi.post('/ajax/pages/Grtes/ttst3', requestBody)
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: "審核成功",
        });
        readyallow(response.data.message);

        // 刷新或更新管理群組的資料
        // loadManageGroups();
<<<<<<< HEAD
        
=======

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message || "審核失敗",
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "審核錯誤：" + (error.response && error.response.data.message ? error.response.data.message : error.message),
      });
    });
}

function removeMemeber(member) {
  // 這裡的 body 是需要提交的數據

  const requestBody = {
<<<<<<< HEAD
        id: member.groupId, // 群組id
        gid: member.userId // 或者其他合適的值
=======
    id: member.groupId, // 群組id
    gid: member.userId // 或者其他合適的值
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  };
  Swal.fire({
    text: "正在移除中...",
    allowOutsideClick: false,
    showConfirmButton: false,
  });
  // 管理者id
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  axiosapi.post('/ajax/pages/Group/myGroup/memberemove', requestBody)
    .then(response => {
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          text: "成員移除成功",
        });
        // 刷新或更新管理群組的資料
        // loadManageGroups();
        manageGroup(member.groupId);
        // manageGroup();
      } else {
        Swal.fire({
          icon: "warning",
          text: response.data.message || "移除失敗",
        });
      }
    })
    .catch(error => {
      Swal.fire({
        icon: "error",
        text: "移除錯誤：" + (error.response && error.response.data.message ? error.response.data.message : error.message),
      });
    });
}


onMounted(() => {
  loadMyGroups();
<<<<<<< HEAD
=======
  // setInterval(loadMyGroups, 20000); 
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
});
</script>

<style scoped>
.container {
<<<<<<< HEAD
    padding: 20px;
}

.create-group, .search-group, .chat-inputs, .manage-group {
    margin-bottom: 20px;
}

input, select, button {
    margin-right: 10px;
    margin-bottom: 10px;
    padding: 5px 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.group-box, .group-item {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 10px;
    margin-bottom: 10px;
    background-color: #f9f9f9;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.group-image, .group-no-image {
    width: 50px;
    height: 50px;
    object-fit: cover;
    margin-right: 10px;
}

.group-no-image {
    background-color: #ddd;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
}

.group-info {
    display: flex;
    flex-direction: column;
}

.group-category {
    cursor: pointer;
    font-weight: bold;
    margin: 10px 0;
=======
  padding: 20px;
}

.create-group,
.search-group,
.chat-inputs,
.manage-group {
  margin-bottom: 20px;
}

input,
select,
button {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 5px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.group-box,
.group-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f9f9f9;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.group-image,
.group-no-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
  margin-right: 10px;
}

.group-no-image {
  background-color: #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
}

.group-info {
  display: flex;
  flex-direction: column;
}

.group-category {
  cursor: pointer;
  font-weight: bold;
  margin: 10px 0;
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

.chat-room-popup {
  position: fixed;
<<<<<<< HEAD
  bottom: 20px; /* 將窗口固定在頁面的底部 */
  right: 20px; /* 距離右邊的距離 */
  width: 800px; /* 設定固定寬度 */
  max-height: 100vh; /* 最大高度設置為視口高度的80% */
  overflow: auto; /* 當內容超過彈出窗口時，顯示滾動條 */
=======
  bottom: 20px;
  /* 將窗口固定在頁面的底部 */
  right: 20px;
  /* 距離右邊的距離 */
  width: 800px;
  /* 設定固定寬度 */
  max-height: 100vh;
  /* 最大高度設置為視口高度的80% */
  overflow: auto;
  /* 當內容超過彈出窗口時，顯示滾動條 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  background: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
<<<<<<< HEAD
  z-index: 1000; /* 確保彈出窗口在最上層 */
=======
  z-index: 1000;
  /* 確保彈出窗口在最上層 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

.chat-room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
  background: #f5f5f5;
}

.close-button {
  background: red;
  color: white;
  border: none;
  border-radius: 50%;
<<<<<<< HEAD
  width: 30px; /* 圓圈的直徑 */
  height: 30px; /* 圓圈的直徑 */
  text-align: center;
  line-height: 30px; /* 垂直對齊文本 */
  font-size: 18px; /* 調整字體大小使X顯示清晰 */
  cursor: pointer;
  display: inline-flex; /* 使用 inline-flex 確保按鈕在容器中對齊 */
  align-items: center; /* 垂直對齊內容 */
  justify-content: center; /* 水平對齊內容 */
=======
  width: 30px;
  /* 圓圈的直徑 */
  height: 30px;
  /* 圓圈的直徑 */
  text-align: center;
  line-height: 30px;
  /* 垂直對齊文本 */
  font-size: 18px;
  /* 調整字體大小使X顯示清晰 */
  cursor: pointer;
  display: inline-flex;
  /* 使用 inline-flex 確保按鈕在容器中對齊 */
  align-items: center;
  /* 垂直對齊內容 */
  justify-content: center;
  /* 水平對齊內容 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

.close-button:hover {
  background: darkred;
}

.chat-box {
  display: flex;
  flex-direction: column;
<<<<<<< HEAD
  height: 400px; /* 調整高度以符合需求 */
=======
  height: 400px;
  /* 調整高度以符合需求 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  overflow-y: scroll;
}

.chat-message {
  display: flex;
  padding: 10px;
  margin: 5px 0;
  border-radius: 10px;
  max-width: 70%;
}

.chat-message.self {
  align-self: flex-end;
<<<<<<< HEAD
  background-color: #c56777; /* 可以根據需要調整顏色 */
=======
  background-color: #c56777;
  /* 可以根據需要調整顏色 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

.chat-message:not(.self) {
  align-self: flex-start;
<<<<<<< HEAD
  background-color: #2ac42a; /* 可以根據需要調整顏色 */
}

.chat-image {
  max-width: 200px; /* 根據需要調整 */
  max-height: 200px; /* 根據需要調整 */
=======
  background-color: #2ac42a;
  /* 可以根據需要調整顏色 */
}

.chat-image {
  max-width: 200px;
  /* 根據需要調整 */
  max-height: 200px;
  /* 根據需要調整 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  margin-top: 5px;
}

button {
<<<<<<< HEAD
    cursor: pointer;
=======
  cursor: pointer;
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

.chat-inputs {
  display: flex;
  align-items: center;
}

.chat-inputs input[type="text"] {
  flex: 1;
  margin-right: 10px;
}


button:hover {
<<<<<<< HEAD
    background-color: #eee;
=======
  background-color: #eee;
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

/* 聊天室內成員圖片 */
.member-info {
  display: flex;
  align-items: center;
<<<<<<< HEAD
  margin-bottom: 5px; /* 控制名稱和訊息之間的間距 */
}

.member-image {
  width: 40px; /* 調整成員圖片大小 */
  height: 40px; /* 調整成員圖片大小 */
=======
  margin-bottom: 5px;
  /* 控制名稱和訊息之間的間距 */
}

.member-image {
  width: 40px;
  /* 調整成員圖片大小 */
  height: 40px;
  /* 調整成員圖片大小 */
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.member-name {
  font-weight: bold;
  margin-right: 10px;
}

#sidebar {
<<<<<<< HEAD
    position: fixed;
    top: 200px;
    right: 20px;
    width: 250px;
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 10px;
    background-color: #f9f9f9;
}

#sidebar ul {
    list-style-type: none;
    padding: 0;
}

#sidebar li {
    margin-bottom: 10px;
=======
  position: fixed;
  top: 200px;
  right: 20px;
  width: 250px;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  background-color: #f9f9f9;
}

#sidebar ul {
  list-style-type: none;
  padding: 0;
}

#sidebar li {
  margin-bottom: 10px;
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}

/* 管理群組的X關閉按鈕 */
.manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

/* 管理部分 加上 成員卡 圖片及成員名稱 */
.member-list {
  list-style: none;
  padding: 0;
}

.member-card {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.member-info {
  display: flex;
  align-items: center;
}

.member-image {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.member-details {
  display: flex;
  flex-direction: column;
}

.member-actions {
  margin-left: auto;
  display: flex;
  gap: 10px;
}
<<<<<<< HEAD


=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
</style>