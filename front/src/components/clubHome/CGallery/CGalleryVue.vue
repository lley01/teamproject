<template>
<div>
<CHHeaderVue style="height: 150px;"></CHHeaderVue>
  <v-app>
    <v-main style="padding: 10px;">      
      <v-row dense>
        <!-- 사이드 -->
        <v-col sm="2"></v-col>
        
        <!-- 메인 -->
        <v-col sm="8">
          <v-row dense="" style="border-bottom: 1px solid #CCC;">
            <v-col sm="6">
              <h5><router-link :to="{ name: 'CHomeVue', query: { cno: state.cno } }">클럽홈</router-link> > {{state.galleryName}}</h5>
            </v-col>                                
          </v-row>

          <v-row dense style="padding-top: 10px; padding-bottom: 5px; padding-left: 10px; ">
            <v-col class="col_left">
              <h2>갤러리</h2>
            </v-col>

            <v-col sm="8" class="col_right">
              <v-select variant="outlined" density="compact" :items="state.items" v-model="state.option" style="height: 40px; padding-right: 10px;" ></v-select>
              <input type="text" class="board_search_box" style="outline-width: 0;" v-model="state.search">
              <v-btn style="height: 40px;" @click="search"><h4>검색</h4></v-btn>
                <v-btn style="margin-left: 10px; height: 40px; background-color: gold;" @click="handleUpload">
                  <h4>업로드</h4>
                </v-btn>
            </v-col>
          </v-row>

          <v-row dense style="border: 1px solid #CCC; border-top: 1px solid #CCC; padding: 10px; padding-left: 20px;  ">
            <v-col v-for="(tmp, idx) in state.gallery" :key="tmp" cols="4">
              <v-card height="300px" class="club_card" style="padding: 20px;" @click="content(tmp.cgno)">
                <v-row dense>
                  <v-col class="col_center" style="height: 200px;">
                    <img :src="`/cluver/clubgallery/image?cgno=${tmp.cgno}&idx=0`" style="max-width: 100%; max-height: 100%; padding: 5px; border: 1px solid #CCC;" />
                  </v-col>
                </v-row>

                <v-row dense style="padding-top: 5px;">
                  <v-col sm="8">
                    <h4>{{tmp.cgname}}</h4>
                  </v-col>

                  <v-col class="col_right">
                    <h5 style="color: gray">조회 {{tmp.cghit}}</h5>
                    <a><img :src="require('../../../assets/img/thumb.png')" style="width: 15px; margin-left: 10px; margin-right: 3px;"/></a>
                    <h5 style="color: gray;">{{tmp.like}}</h5>
                  </v-col>
                </v-row>

                <v-row dense>
                  <v-col style="padding-left: 10px;">
                    <td v-if="state.nicklist[idx].mcname === null">{{ state.nicklist[idx].mpnickname}}</td>
                    <td v-if="state.nicklist[idx].mpnickname === null">{{ state.nicklist[idx].mcname}}</td>
                  </v-col>

                  <v-col sm="6" class="col_right">
                    <h5 style="color: gray">{{tmp.cgregdate1}}</h5>
                  </v-col>
                </v-row>
              </v-card>
            </v-col>
          </v-row>
        </v-col>

        <!-- 사이드 -->
        <v-col sm="2"></v-col>
      </v-row>

      <v-row dense style="margin-top: 10px;">
        <v-col>
          <v-pagination
          v-model="state.page"
          :length="state.pages"
          @click="handlePage(state.page, state.option, state.search)"
          ></v-pagination>
        </v-col>
      </v-row>
    </v-main>
  </v-app>
  <FooterVue></FooterVue>
</div>
</template>

<script>
import { reactive }  from '@vue/reactivity';
import { useRoute, useRouter } from 'vue-router';
import FooterVue     from '../../FooterVue.vue';
import CHHeaderVue   from '../CHHeaderVue.vue';
import {onMounted} from 'vue';
import axios from 'axios';
import dayjs from 'dayjs';

export default {
  components: { CHHeaderVue, FooterVue },
  setup () {
    const router = useRouter();
    const route = useRoute();

    const state = reactive({
      gallery : [],
      page: 1,   // 현재 페이지
      pages : 1, // 총 페이지 수
      items: [
        '전체', '갤러리명', '갤러리설명', '갤러리생성자'
      ],
      search : '',
      option: '전체',
      cno : route.query.cno,
      token : sessionStorage.getItem("TOKEN"),
      nicklist : [],
      galleryName : '갤러리'
    });

    const selectlist = async() => {
      if(state.token !== null) {
        const url      = `/cluver/api/clubgallery/selectlist?page=${state.page}&cno=${state.cno}`;
        const headers  = {"Content-Type":"application/json", "token" : state.token};
        const response = await axios.get(url, {headers});
        console.log(response.data);

        if(response.data.status === 200) {
          state.pages = response.data.result.pages;
          state.gallery = response.data.result.list;
          state.nicklist.splice(0); // state.nicklist 초기화 //페이지 이동 시 닉네임 목록 갱신
          state.nicklist = response.data.result.mlist;

          for(var i = 0; i < state.gallery.length; i++) {
            date(i);
          }
        }
      }
      else if(response.data.status === 0)
      {
        alert("로그인이 필요한 페이지입니다.");
        router.push({ name: 'LoginVue' });
      }
      else
      {
        alert('비정상적인 접근입니다.');
        router.push({ name: 'HomeVue' });
      }
    }

    const date = (i) => {
      // console.log(state.gallery[i].cgregdate);
      state.gallery[i].cgregdate1 = dayjs(state.gallery[i].cgregdate).format('YY.MM.DD hh:mm:ss');
    }

    const handlePage = async(idx, option, search) => {
      if(state.token !== null) {
        const url      = `/cluver/api/clubgallery/selectlist?page=${idx}&text=${search}&option=${option}&cno=${state.cno}`;
        const headers  = {"Content-Type":"application/json", "token" : state.token};
        const response = await axios.get(url, {headers});
        // console.log(response.data);
        if(response.data.status === 200) {
          state.gallery = response.data.result.list;
          state.nicklist.splice(0); // state.nicklist 초기화 //페이지 이동 시 닉네임 목록 갱신
          state.nicklist = response.data.result.mlist;
          for(var i = 0; i < state.gallery.length; i++) {
            date(i);
          }
        }
      }else if(response.data.status === 0) {
        alert("로그인이 필요한 페이지입니다.");
        router.push({name:'LoginVue'});
      }else{
        alert('비정상적인 접근입니다.');
        router.push({name:'HomeVue'});
      }
    }

    const content = (cgno) => {
      router.push({ name: "CGContentVue", query: { cgno :cgno, cno :state.cno } });
    }

    const search = async() => { // 전체검색기능 미구현
      if(state.token !== null) {
          const url      = `/cluver/api/clubgallery/selectlist?page=${state.page}&text=${state.search}&option=${state.option}&cno=${state.cno}`;
          const headers  = { "Content-Type": "application/json", "token": state.token };
          const response = await axios.get(url, {headers});
          // console.log(response.data);

          if(response.data.status === 200) {
            state.gallery = response.data.result.list;
            state.pages = response.data.result.pages;
            state.nicklist.splice(0); // state.nicklist 초기화 //페이지 이동 시 닉네임 목록 갱신
            state.nicklist = response.data.result.mlist;
            for(var i = 0; i < state.gallery.length; i++) {
              date(i);
            }
          }
        }else{
          router.push({name:'LoginVue'});
        }
    }

    const handleUpload = () => {
      router.push({name:'CGUploadVue', query:{cno : state.cno}});
    }

    onMounted(() => {
      selectlist();
    });

    return { state, content, search, handleUpload, handlePage }
  }
}
</script>

<style lang="scss" scoped>
/* Helper classes */
.bg-basil {
  background-color: #FFFBE6 !important;
}

.text-basil {
  color: #356859 !important;
}

@font-face {
  font-family: 'HallymGothic-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2204@1.0/HallymGothic-Regular.woff2') format('woff2');
  font-weight: 400;
  font-style: normal;
}

.select {
  min-width: 150px;
  padding-right: 10px;
}
</style>