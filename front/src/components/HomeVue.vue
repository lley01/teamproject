<template>
<div>
<HeaderVue style="height: 220px;"></HeaderVue>
    <v-app id="inspire"> 
        <!-- 메인 리스트 -->
        <v-main>
            <div style="padding: 20px;">
                <v-row>
                    <v-col sm="2"><img :src="require('../assets/img/banner_left.jpg')" style="width: 100%;"/></v-col>

                    <v-col>
                        <v-row>
                            <v-col sm="9">
                                <v-row>
                                    <v-col sm="8">
                                        <v-card height="150" style="border-width: 4px; border-color: gold;">
                                            <vueper-slides ref="myVueperSlides" autoplay fixed-height="100%">
                                                <vueper-slide
                                                    v-for="tmp1 in state.slides" :key="tmp1" :title="tmp1.title"
                                                    :image="tmp1.image" />
                                            </vueper-slides>
                                        </v-card>
                                    </v-col>

                                    <v-col sm="4">
                                        <v-card height="150" style="padding: 10px; border-width: 4px; border-color: gold;">
                                            <v-row dense style="padding-bottom: 10px;">
                                                <v-col sm="5" class="col_left"><h4>공지사항</h4></v-col>

                                                <v-col sm="5"></v-col>

                                                <v-col sm="2" class="col_right">
                                                    <router-link to="/hqna" class="col_right">
                                                        <v-icon style="margin-top: -5px;" icon="mdi-arrow-right-bold" />
                                                    </router-link>
                                                </v-col>
                                            </v-row>

                                            <v-row dense @click="notice()" style="cursor: pointer;">
                                                <v-col sm="3">
                                                    <h4 style="color: navy;">[공지]</h4>
                                                </v-col>

                                                <v-col class="col_left">
                                                    <h4>{{state.notice}}</h4>
                                                </v-col>
                                            </v-row>

                                            <v-row dense @click="notice()" style="cursor: pointer;">
                                                <v-col sm="3">
                                                    <h4 style="color: navy;">[공지]</h4>
                                                </v-col>

                                                <v-col class="col_left">
                                                    <h4>{{state.notice1}}</h4>
                                                </v-col>
                                            </v-row>
                                            
                                            <v-row dense @click="notice()" style="cursor: pointer;">
                                                <v-col sm="3">
                                                    <h4 style="color: navy;">[공지]</h4>
                                                </v-col>

                                                <v-col class="col_left">
                                                    <h4>{{state.notice2}}</h4>
                                                </v-col>
                                            </v-row>
                                        </v-card>
                                    </v-col>
                                </v-row>

                                <v-row>
                                    <v-col sm="6" class="home_box">
                                        <router-link to="/clist">
                                            <v-card height="150" style="border-width: 4px; border-color: gold;">
                                                <v-row dense>
                                                    <v-col class="col_center"><h3 style="margin-top: 10px;">지역별 클럽</h3></v-col>
                                                </v-row>

                                                <v-row dense>
                                                    <v-col style="justify-content: center; display: flex;">
                                                        <a class="region">전국</a> <a class="region">서울</a> <a class="region">경기</a> <a class="region">인천</a>
                                                        <a class="region">부산</a> <a class="region">대전</a> <a class="region">대구</a> <a class="region">울산</a>
                                                    </v-col>
                                                </v-row>
                                                
                                                <v-row dense>
                                                    <v-col style="justify-content: center; display: flex;">
                                                        <a class="region">강원</a> <a class="region">충북</a> <a class="region">충남</a> <a class="region">전북</a>
                                                        <a class="region">전남</a> <a class="region">경북</a> <a class="region">경남</a> <a class="region">제주</a> 
                                                    </v-col>
                                                </v-row>

                                                <v-row dense>
                                                    <v-col sm="1"></v-col>

                                                    <v-col sm="10" style="margin-top: 5px; border-top: 1px solid #CCC; justify-content: center; display: flex;">
                                                        <v-row dense="">
                                                            <v-col sm="6" class="col_center"><a>온라인</a></v-col>
                                                            <v-col sm="6" class="col_center"><a>준비중</a></v-col>
                                                        </v-row>
                                                    </v-col>

                                                    <v-col sm="1"></v-col>
                                                </v-row>
                                            </v-card>
                                        </router-link>
                                    </v-col>

                                    <v-col sm="6">
                                        <v-card height="150" style="border-width: 4px; border-color: gold;">
                                            <v-row dense>
                                                <v-col class="col_center"><h3 style="margin-top: 10px;">테마별 클럽</h3></v-col>
                                            </v-row>

                                            <v-row dense>
                                                <v-col sm="1"></v-col>

                                                <v-col sm="10" style="justify-content: center; display: flex;">
                                                    <a class="region">스포츠</a> <a class="region">헬스</a> <a class="region">음악</a> <a class="region">영화</a>
                                                    <a class="region">IT</a> <a class="region">아웃팅</a> <a class="region">여행</a> 
                                                </v-col>

                                                <v-col sm="1"></v-col>
                                            </v-row>
                                            
                                            <v-row dense>
                                                <v-col sm="1"></v-col>

                                                <v-col sm="10" style="justify-content: center; display: flex; border-bottom: 1px solid #CCC;">
                                                    <a class="region">게임</a> <a class="region">스터디</a> <a class="region">캠핑</a> <a class="region">주식/재테크</a>
                                                    <a class="region">자기계발</a>
                                                </v-col>

                                                <v-col sm="1"></v-col>
                                            </v-row>

                                            <v-row dense>
                                                <v-col sm="1"></v-col>

                                                <v-col sm="10" style="margin-top: 5px; justify-content: center; display: flex;">
                                                    <v-row dense="">
                                                        <v-col sm="4" class="col_center"><a>연령별</a></v-col>
                                                        <v-col sm="4" class="col_center"><a>준비중</a></v-col>
                                                        <v-col sm="4" class="col_center"><a>준비중</a></v-col>
                                                    </v-row>
                                                </v-col>

                                                <v-col sm="1"></v-col>
                                            </v-row>
                                        </v-card>
                                    </v-col>
                                </v-row>
                            </v-col>

                            <v-col sm="3" style="align:center;" v-if="!state.logged">
                                <v-row>
                                    <v-col>
                                        <router-link to="/login">
                                            <v-card height="150" style="padding-top: 6%; border-width: 4px; border-color: gold;">
                                                <v-row dense>
                                                    <v-col class="col_center">
                                                        <img :src="require('../assets/img/login.png')" style="width:80px;"/>
                                                    </v-col>
                                                </v-row>
                                                
                                                <v-row dense><v-col class="col_center"><h3>로그인</h3></v-col></v-row>
                                            </v-card>
                                        </router-link>
                                    </v-col>
                                </v-row>

                                <v-row>
                                    <v-col>
                                        <router-link to="/join">
                                            <v-card height="150" style="padding-top: 6%; border-width: 4px; border-color: gold; align-item: center;">
                                                <v-row dense>
                                                    <v-col class="col_center">
                                                        <img :src="require('../assets/img/join.png')" style="width:80px;"/>
                                                    </v-col>
                                                </v-row>
                                                
                                                <v-row dense><v-col class="col_center"><h3>회원가입</h3></v-col></v-row>
                                            </v-card>
                                        </router-link>
                                    </v-col>
                                </v-row>
                            </v-col>

                            <v-col sm="3" style="align:center;" v-if="state.logged">
                                <v-row dense>
                                    <v-col>
                                        <v-card style="padding: 10px; border-width: 4px; border-color: gold; align-item: center; height: 313px;">
                                            <v-row dense>
                                                <v-col sm="6">
                                                    <img  :src="state.imageUrl"  style="width: 100px; border: 1px solid #CCC;"/>
                                                </v-col>

                                                <v-col sm="6">
                                                    <v-row dense>
                                                        <v-col class="col_right">
                                                            <router-link to="/activity"><img :src="require(`../assets/img/bell.png`)" style="width: 20px"/></router-link>
                                                        </v-col>
                                                    </v-row>

                                                    <v-row dense>
                                                        <v-col sm="8" v-if="state.cname" style="padding-top: 5px;">
                                                            <div class="col_center" style="background-color: navy; color: white; width: 100px; border: 0px solid; border-radius: 6px; height: 60px;"><h3>기업회원</h3></div>
                                                        </v-col>
                                                        
                                                        <v-col sm="8" v-if="state.nick" style="padding-top: 5px;">
                                                            <div class="col_center" style="background-color: gold; width: 100px; border: 0px solid; border-radius: 6px; height: 67px;"><h3>개인회원</h3></div>
                                                        </v-col>
                                                    </v-row>
                                                </v-col>
                                            </v-row>

                                            <v-row dense style="padding-left: 10px; padding-right: 10px; height: 155px;">
                                                <v-col>
                                                    <v-row v-if="state.cname">
                                                        <v-col class="col_left" >
                                                            <h4>{{state.cname.mcname}}</h4>님 환영합니다!
                                                        </v-col>
                                                    </v-row>

                                                    <v-row v-if="state.nick">
                                                        <v-col class="col_left" >
                                                            <h4>{{state.nick.mpnickname}}</h4>님 환영합니다!
                                                        </v-col>
                                                    </v-row>

                                                    <v-row dense v-if="state.cname">
                                                        <v-col class="col_center">
                                                            <router-link to="/regclub">
                                                                <v-btn style="width: 100px; height: 100px; margin-right: 5px;"><h3>클럽생성</h3></v-btn>
                                                            </router-link>

                                                            <router-link to="/cmypage">
                                                                <v-btn style="width: 100px; height: 100px; margin-left: 5px;"><h3>마이페이지</h3></v-btn>
                                                            </router-link>
                                                        </v-col>
                                                    </v-row>

                                                    <v-row dense v-if="state.nick">
                                                        <v-col class="col_center">
                                                            <router-link to="/regclub">
                                                                <v-btn style="width: 100px; height: 100px; margin-right: 5px;"><h3>클럽생성</h3></v-btn>
                                                            </router-link>

                                                            <router-link to="/mypage">
                                                                <v-btn style="width: 100px; height: 100px; margin-left: 5px;"><h3>마이페이지</h3></v-btn>
                                                            </router-link>
                                                        </v-col>
                                                    </v-row>

                                                </v-col>
                                            </v-row>

                                            <v-row dense>
                                                <v-col sm="4" class="col_center">
                                                    <router-link to="/mypage"><h4>내정보</h4></router-link>
                                                </v-col>

                                                <v-col sm="4" class="col_center">
                                                    <router-link to="/likelist"><h4>찜목록</h4></router-link>
                                                </v-col>

                                                <v-col sm="4" class="col_center">
                                                    <router-link to="/logout"><h4>로그아웃</h4></router-link>
                                                </v-col>
                                            </v-row>
                                        </v-card>
                                    </v-col>
                                </v-row>
                            </v-col>
                        </v-row>

                        <v-row style="border-top: 1px solid #CCC; margin-top: 20px;"><h4 style="padding: 10px;">슈퍼 클럽</h4></v-row>

                        <v-row v-if="state.items1">
                            <v-col v-for="(items, idx) in state.items1" :key="items" cols="4">
                                <v-card height="200px" class="club_card" style="padding: 20px;">
                                    <v-row dense>
                                        <v-col sm="3"></v-col>
                    
                                        <v-col sm="6" class="col_center">
                                            <img v-if="items.imgurl" :src="items.imgurl" @click="handlePage(items.obj.cno, idx)" style="height: 50px; cursor: pointer;"/>
                                            <img v-if="!items.imgurl" :src="require(`../assets/img/default-logo.jpg`)" @click="handlePage(items.obj.cno, idx)" style="height: 50px; cursor: pointer;"/>
                                        </v-col>

                                        <v-col sm="3" class="col_right">
                                            <v-btn style="height: 100%; width: 10px;" id="like"  @click="changeheart(items.obj.cno,idx)">
                                                <img v-if="state.imgcheck[idx].type === 0" :src="state.imgName"  style="width: 30px"/>
                                                <img v-if="state.imgcheck[idx].type === 1" :src="state.imgName1"  style="width: 30px"/>
                                            </v-btn>
                                        </v-col>
                                    </v-row>
                                    
                                    <v-row dense>
                                        <v-col>
                                            <h3 @click="handlePage(items.obj.cno, idx)" style="cursor: pointer;">{{items.obj.cname}}</h3>
                                        </v-col>
                                    </v-row>

                                    <v-row dense style="height:60px;">
                                        <v-col>
                                            <h4 style="display: flex">{{items.obj.cdesc}}</h4>
                                        </v-col>
                                    </v-row>

                                    <v-row dense style="vertical-align: baseline;">
                                        <v-col>
                                            <h4>{{items.obj.carea}}</h4>
                                        </v-col>
                                    </v-row>
                                </v-card>
                            </v-col>
                        </v-row>
                    </v-col>

                    <v-col sm="2"><img :src="require('../assets/img/banner_right.jpg')" style="width:100%;" /></v-col>
                </v-row>
            </div>
        </v-main>
    </v-app>
<FooterVue></FooterVue>
</div>
</template>

<script>
import { reactive } from '@vue/reactivity';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex'
import { VueperSlides, VueperSlide } from 'vueperslides'
import 'vueperslides/dist/vueperslides.css'
import FooterVue from '../components/FooterVue.vue';
import HeaderVue from '../components/HeaderVue.vue';
import { onMounted, computed } from '@vue/runtime-core';
import axios from 'axios';

export default {
    components: { VueperSlides, VueperSlide, HeaderVue, FooterVue },
    setup () {
        const router = useRouter();
        const store = useStore();

        const state = reactive({
            likecheck: [],
            imgcheck : [],
            imgName  :  require(`../assets/img/heart.png`),
            imgName1 : require(`../assets/img/heart1.png`),
            role1    : 'PERSONAL',
            role2    : 'COMPANY',
            role3    : '',
            imageUrl1: '',
            imageUrl : require('../assets/img/profile_sample.png'),
            logged   : computed(() => store.getters['moduleA/getLogged']),
            token    : sessionStorage.getItem("TOKEN"),
            logo     : 'club_logo',
            notice   : '클러버들 환영합니다',
            notice1  : '개인정보 이용 약관...',
            notice2  : '불법행위 적발시 처벌...',

            slides: [
                { title: '', image: require('../assets/img/ad1.jpg') },
                { title: '', image: require('../assets/img/ad2.jpg') },
                { title: '', image: require('../assets/img/ad3.jpg') }
            ],

            card: {
                clubname: '',
                desc    : '',
                desc1   : '',
                area1   : '',
                area2   : '',
            },

            profile: {
                nickname: '',
            },            
        });

        onMounted (()=>{
            handleData(), Likelist();

            if(state.token != null){
                role(), mypage(), nick();
            }

            if(state.token === null){
                store.commit('moduleA/setLogged', false)
            }else {
                store.commit('moduleA/setLogged', true)
            }
            if (state.card.desc.length >= 40) {
                state.card.desc1 = state.card.desc.substring(0, 40) + '...'
            }            
        });

        const handlePage = async(cno) => {
            router.push({ name: "ClubDetailVue", query: { cno: cno }})
            console.log(cno);
        }

        const handleData = async() => {
            const url      = `/cluver/club/selectlist2`;
            const headers  = { "Content-Type": "application.json" };
            const response = await axios.get(url, { headers: headers });
            console.log(response.data.result);

            if(response.data.status === 200) {
                state.items1 = response.data.result;
                for(var i =0; i < state.items1.length; i++){
                    state.imgcheck.push({cno:state.items1[i].obj.cno,type:0})
                }                   
            }    
        }

        const mypage = async() => {
            const url      = `/cluver/member/mypage`;
            const headers  = { "Content-Type": "application/json", token: state.token };
            const response = await axios.get(url, {headers});
            console.log(response.data.result);

            if(response.data.status === 200) {
                state.items = response.data.result;
                if(state.items.mprofile != "") {
                    if(state.items.mprofile != null) {
                        state.imageUrl  = response.data.result.mimageurl
                        state.imageUrl1 = response.data.result.mimageurl
                    }
                }else {
                    state.imageUrl = '';
                }
            }
        }
        
        const Likelist = async() => {
            const url      = `/cluver/api/like/selectlist`;
            const headers  = { "Content-Type": "application.json", token: state.token };
            const response = await axios.get(url,{ headers: headers });
            console.log(response.data);
            if(response.data.status === 200){
                state.likelist = response.data.result;

                for(var j = 0; j < state.imgcheck.length; j++) {

                    for(var i = 0; i < state.likelist.length; i++){
                        if(state.imgcheck[j].cno === Number(state.likelist[i].clubCno)) {
                            state.imgcheck[j].type = 1;
                        }
                    }
                }
            console.log(state.likelist);
            }
        }

        const cname = async() => {
            const url      = `/cluver/member/cname`;
            const headers  = { "Content-Type": "application/json", token: state.token };
            const response = await axios.get(url, { headers });
            console.log(response.data.result);

            if(response.data.status === 200){
                state.cname = response.data.result;
            }
        }

        const nick = async() => {
            const url      = `/cluver/member/psmynick`;
            const headers  = { "Content-Type": "application/json", token: state.token };
            const response = await axios.get(url, {headers});
            console.log(response.data.result);
            if(response.data.status === 200){
                state.nick = response.data.result;
            }else {
                role();
                console.log("기업회원입니다");
            }
        }

        const role = async() => {
            const url      = `/cluver/member/role`;
            const headers  = { "Content-Type": "application/json", token: state.token };
            const response = await axios.get(url, { headers });
            console.log(response.data.result);

            if(response.data.status === 200){
                state.role = response.data.result;
                if(state.role === 'PERSONAL'){
                    // handlenick();
                }
                if(state.role === 'COMPANY'){
                    cname();
                }
            }
        }

        const changeheart = async(cno, idx) => {
            console.log(state.likelist.clubCno);
            console.log(cno);
            if(state.imgcheck[idx].cno === cno ) {
                if(state.imgcheck[idx].type === 1) {
                    state.imgcheck[idx].type =0;
                }else {
                    state.imgcheck[idx].type = 1;
                }
            }
            const url      = `/cluver/api/like/insert`
            const headers  = { "Content-Type":"multipart/form-data", token: state.token };
            const body     = new FormData;
            body.append("club", cno);
            const response = await axios.post(url, body, { headers: headers });
            console.log(response.data);
                if(response.data.status == 200){
                    console.log("찜하기성공");
                }
                if(response.data.status == -1){
                    console.log("찜하기 취소");
                    unlike(cno, idx);
                }
                    
            };

        const unlike = async(cno,idx) => {
            console.log("unlike", state.imgcheck[idx]);
            const url      = `/cluver/api/like/deleteone`
            const headers  = {"Content-Type":"multipart/form-data", token : state.token};
            const body     = new FormData;
            body.append("club", cno);   
            const response = await axios.post(url, body, { headers: headers });
            console.log(response.data);

            if(response.data.status == 200){
                // state.imgName = state.imaName1
            }
        }

        const notice = () => {
            router.push({ name: "HelpQnaContentVue" });
        };

        const handlelogout = () => {
            if(confirm('로그아웃 하시겠습니까?'))
            router.push({ name: "LogoutVue" });
        };

        const clubDetail = () => {
            router.push({ name: "ClubDetailVue" });
        }

        return { state, changeheart, handlelogout,handlePage, notice, clubDetail, unlike }
    },
}
</script>

<style lang="scss" scoped>

</style>