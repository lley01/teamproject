<template>
<div v-if="state.items">
<HeaderVue style="height: 150px;"></HeaderVue>
    <v-app>
        <v-main style="padding: 10px;">      
            <v-row dense>
                <v-col sm="2"></v-col>
                
                <v-col sm="8">
                    <v-row dense="" style="padding-bottom: 4px;">
                        <v-col sm="6">
                            <h5><router-link to="/">홈</router-link> > <router-link to="/cdetail">클럽목록</router-link> > 상세정보</h5>
                        </v-col>

                        <v-col class="col_right">
                            <h5>등록일: {{state.regdate1}}
                                 <!-- 수정일: {{state.updateDate}} -->
                                 </h5>
                        </v-col>
                    </v-row>
                    <v-row dense class="club_detail_box2" style="border-bottom: 1px solid #CCC;">
                        <v-col sm="7">
                            <v-row dense>
                                <v-col sm="6">
                                    <h4>{{state.items.club.cname}}</h4>
                                </v-col>

                                <v-col sm="6" class="col_right">
                                    <!-- <h4>({{state.now}}/{{state.limit}})</h4> -->
                                </v-col>
                            </v-row>

                            <v-row dense>
                                <v-col>
                                    <h2>{{state.items.cdtitle}}</h2>
                                </v-col>
                            </v-row>
                        </v-col>

                        <v-col sm="3" class="col_center">
                            <!-- <img  v-if="state.imgurl" :src="state.imgurl" style=" height: 80px; width: 150px;"/>
                            <img  v-if="!state.imgurl" :src="require(`../../assets/img/default-logo.jpg`)"  style="height: 50px; cursor: pointer;"/> -->
                        </v-col>

                        <v-col sm="1" style="vertical-align: middle;">
                            <v-btn style="height: 100%; width: 100%;" @click="chome(cno)">
                                <img :src="require(`../../assets/img/home_icon.png`)" style="width: 40px"/>
                            </v-btn>
                        </v-col>
                        
                        <v-col sm="1" class="col_right">
                            <v-btn style="height: 100%; width: 100%;" id="like"  @click="changeheart(state.items.club.cno)">
                                <img v-if="state.imgcheck === 0" :src="state.imgName" style="width: 40px"/>
                                <img v-if="state.imgcheck === 1" :src="state.imgName1" style="width: 40px"/>
                            </v-btn>
                        </v-col>
                    </v-row>

                    <v-row dense class="club_detail_box1" style="border: 3px solid;">
                        <v-row dense>
                            <!-- 왼쪽 -->
                            <v-col sm="8" style="padding: 10px;">
                                <v-row dense style="padding-bottom: 10px; border-bottom: 1px solid #CCC;">
                                    <v-col sm="6">
                                        <v-carousel cycle hide-delimiters show-arrows="hover" style="height:150px;"> 
                                            <v-carousel-item v-for="item in 1" 
                                            :key="item" :src="state.imgurl" cover></v-carousel-item>
                                        </v-carousel>
                                    </v-col>

                                    <v-col sm="3">
                                        <v-row dense>
                                            <v-col class="col_center" style="margin-top: 10%; padding-left: 10px;">
                                                <img :src="require('../../assets/img/calendar_icon.png')" style="width: 50px;"/>
                                            </v-col>
                                        </v-row>

                                        <v-row dense>
                                            <v-col class="col_center" style="padding-left: 10px;">
                                                <h4>{{state.items.date}}</h4>
                                            </v-col>
                                        </v-row>
                                    </v-col>

                                    <v-col sm="3">
                                        <v-row dense>
                                            <v-col class="col_center" style="margin-top: 10%; padding-right: 10px;">
                                                <img :src="require('../../assets/img/clock_icon.png')" style="width: 50px;"/>
                                            </v-col>
                                        </v-row>

                                        <v-row dense>
                                            <v-col class="col_center" style="padding-right: 10px;">
                                                <h4>{{state.items.time}}</h4>
                                            </v-col>
                                        </v-row>
                                    </v-col>
                                </v-row>

                                <v-row dense>
                                    <v-col style="padding: 20px;">
                                        <h2>모집조건</h2>
                                    </v-col>
                                </v-row>

                                <v-row dense style="padding-left: 20px;">
                                    <v-col sm="2">
                                        <h4 style="color: #787878">성별</h4>
                                        <h4 style="color: #787878; margin-top: 10px;">연령</h4>
                                        <h4 style="color: #787878; margin-top: 10px;">모집인원</h4>
                                    </v-col>

                                    <v-col sm="10">
                                        <h4>{{state.items.gender}}</h4>
                                        <h4 style="margin-top: 10px;">{{state.items.age}}</h4>
                                        <h4 style="margin-top: 10px;">{{state.howmany}}명</h4>
                                    </v-col>
                                </v-row>
                            </v-col>

                            <!-- 오른쪽 -->
                            <v-col sm="4" style="height: 100%; border-left: 1px solid #CCC; padding: 10px;">
                                <v-row dense>
                                    <v-col style="border-bottom: 1px solid #CCC; padding-bottom: 10px;">
                                            <v-btn style="width: 100%; height: 120px;" @click="handleClick(state.items.club.cno)"><h2>지원하기</h2></v-btn>
                                        <h3 style="padding-top: 10px; padding-left: 10px;">모집기간</h3>
                                        <h4 style="padding-left: 15px;">{{state.regdate1}} ~ {{state.enddate1}}</h4>
                                    </v-col>
                                </v-row>
                                
                                <v-row dense style="padding: 10px;" >
                                    <v-col>
                                        <h3>클럽담당자정보</h3>
                                    </v-col>
                                </v-row>

                                <v-row dense>
                                    <v-col sm="3" style="padding-left: 15px;"> 
                                        <v-row dense>
                                            <v-col>
                                                <h5 style="color: #787878">담당자명</h5>
                                                <h5 style="color: #787878">연락처</h5>
                                                <h5 style="color: #787878">이메일</h5>
                                            </v-col>
                                        </v-row>
                                    </v-col>

                                    <v-col sm="9">
                                        <v-row dense>
                                            <v-col>
                                                <h5>{{state.manager.name}}</h5>
                                                <h5>{{state.manager.number}}</h5>
                                                <h5>{{state.manager.email}}</h5>
                                            </v-col>
                                        </v-row>
                                    </v-col>
                                </v-row>
                            </v-col>
                        </v-row>
                    </v-row>

                    <!-- 장소정보 -->
                    <v-row dense="" style="margin-top: 20px; padding: 20px; border: 3px solid;">
                        <v-col style="padding-left: 22px;">
                            <v-row dense>
                                <h2>장소정보</h2>
                            </v-row>

                            <v-row dense style="padding-left: 20px; margin-top: 10px;">
                                <v-col sm="1">
                                    <h4 style="color: #787878">장소</h4>
                                    <h4 style="color: #787878; margin-top: 10px;">주소</h4>
                                    <h4 style="color: #787878; margin-top: 10px;">지도</h4>
                                </v-col>

                                <v-col sm="11">
                                    <h4>{{state.items.club.carea}}</h4>
                                    <h4 style="margin-top: 10px;">{{state.items.club.caddress}}</h4>
                                    <MapVue style="margin-top: 10px; width: 800px; border: 1px solid #CCC;"></MapVue>
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>

                    <!-- 상세모집내용 -->
                    <v-row dense style="margin-top: 20px; padding: 20px; border: 3px solid;">
                        <v-col style="padding-left: 22px;">
                            <v-row dense>
                                <h2>상세내용</h2>
                            </v-row>

                            <v-row dense style="padding-left: 20px; margin-top: 10px; padding-right: 32px;">
                                <v-col>
                                    <h4><div v-html="state.items.cdcontent"></div></h4>
                                    <!-- <img :src="require('../../assets/img/mozip.jpg')" style="width: 100%;"/> -->
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                </v-col>

                <v-col sm="2"></v-col>
            </v-row>
        </v-main>
    </v-app>
    <FooterVue></FooterVue>
</div>
</template>

<script>
import { reactive } from '@vue/reactivity';
import FooterVue    from '../FooterVue.vue';
import MapVue       from '../MapVue.vue';
import HeaderVue    from '../HeaderVue.vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import { onMounted } from '@vue/runtime-core';
import { useStore } from "vuex";
import dayjs from 'dayjs';

export default {
    components: { HeaderVue, FooterVue, MapVue },
    setup () {
        const route = useRoute();
        const router = useRouter();
        const store = useStore();

        const state = reactive({
            url : '',
            imgurl : '',
            cdno      : '',
            token     : sessionStorage.getItem("TOKEN"),
            cno       : route.query.cno,
            writeDate : '2022년 5월 2일',
            updateDate: '2022년 5월 5일',
            endDate   : '2022년 5월 27일',
            clubname  : '삥뽕탁구클럽',
            title     : '함께 탁구 칠 삥뽕러 모두 모여랏! XD',
            now       : 10,
            limit     : 50,
            date      : '주말',
            time      : '종일',
            gender    : '성별무관',
            age       : '20세 이상(2003년생 ~)',
            howmany   : '0',
            addrname  : '성찬미탁구클럽',
            addr1     : '',
            likecheck : [],
            imgcheck  : 0,
            imgcheck1 : 1,
            imgName   : require(`../../assets/img/heart.png`),
            imgName1  : require(`../../assets/img/heart1.png`),
            regdate   : '',

            manager: {
                name  : '김이박',
                number: '010-4444-78941',
                email : 'a@c.com'
            },
            img: [
                {
                    src: 'http://news.samsungdisplay.com/wp-content/uploads/2017/09/%EB%8F%84%EB%B9%84%EB%9D%BC-11.png',
                },
                {
                    src: 'https://www.sonohotelsresorts.com/upload/image/faci/dmFaci_201607131119187730',
                },
            ]
        });

        onMounted( () => {
            handleData(), Likelist(),handleData1();
        })

        const handleData = async() => {
            const url      = `/cluver/clubdetail/selectcno?cno=${state.cno}`;
            const headers  = { "Content-Type": "application/json" };
            const response = await axios.get(url, {headers});
            console.log(response.data);

            if(response.data.status === 200){
                state.imgurl = response.data.imgurl;
                console.log("IIIIIIIIIIIIII",state.imgurl);
                state.items = response.data.result;
                state.cdno  = response.data.result.cdno;
                state.addr1 = state.items.club.caddress;
                console.log("====",state.items.club.caddress);
                console.log(state.cdno);
                date(); date1();
            }

            handleSend();
        }
        const handleData1 = async() => {
            const url      = `/cluver/clubdetail/detailselectone?cno=${state.cno}`;
            const headers  = { "Content-Type": "application/json" };
            const response = await axios.get(url, {headers});
            console.log(response.data);

            if(response.data.status === 200){
                state.url = response.data.result
                // console.log(state.url);
                console.log("handleData1r",response.data.result);
            }

        }

        const handleClick = async(cno) => {
            console.log(cno);
            router.push({ name: "ClubRequestVue", query: { cno: cno } })
            console.log(cno);
        }
        

        const handleSend = () => {
            store.commit("setCounter", state.addr1);
        }
        // const handleData2 = (a) => {
        //         console.log(a);
        //         state.addr1 = a.address1;
        // }

        const Likelist = async() => {
            const url     = `/cluver/api/like/likeone?cno=${state.cno}`;
            const headers = { "Content-Type": "application.json", "token": state.token };
            const response = await axios.get(url,{headers:headers});
            console.log("찜 ==> ", response.data);

            if(response.data.status === 200){
                console.log("찜");
                state.items1 = response.data.result;
                console.log(state.items1.clubCno);
                state.imgcheck = 1;
                console.log(state.imgcheck);
            }else {
                console.log("찜x");
                state.imgcheck1 === 0;
                console.log(state.imgcheck1);
            }
        }

        // const Likelist1 = async() => {
        //     const url = `/cluver/api/like/likeone?cno=${state.cno}`;
        //     const headers = {"Content-Type":"application.json",
        //     token : state.token};
        //     const response = await axios.get(url,{headers:headers});
        //     console.log(state.items1);
        //     if(state.items1 === null){
        //         state.
        //     }
        // }

        const date = () => {
            console.log(state.items.regdate);
            state.regdate1 = dayjs(state.items.regdate).format('YY.MM.DD hh:mm:ss');
        }

        const date1 = () => {
            console.log(state.items.enddate);
            state.enddate1 = dayjs(state.items.enddate).format('YY.MM.DD hh:mm:ss');
        }

        const changeheart = async(cno) => {
            console.log(state.items.club.cno);
            console.log(cno);
            const url     =`/cluver/api/like/insert`
            const headers = { "Content-Type": "multipart/form-data", "token": state.token };
            const body    = new FormData;
            body.append("club", cno);
            const response = await axios.post(url,body,{headers:headers});
            console.log(response.data);

            if(response.data.status === 200){
                console.log("찜하기 성공");
                state.imgcheck = 1;
            }
            if(response.data.status === -1 ){
                console.log("찜하기 취소");
                state.imgcheck = 0;
                unlike(cno);
            }
        };

        const unlike = async(cno) => {
            console.log("unlike", state.imgcheck);
            const url     = `/cluver/api/like/deleteone`
            const headers = { "Content-Type": "multipart/form-data", "token" :state.token};
            const body    = new FormData;
            body.append("club", cno);   
            const response = await axios.post(url, body, {headers:headers});
            console.log(response.data);

            if(response.data.status == 200){
                // state.imgcheck === 0;
                    // state.imgName = state.imaName1
            }
        }

        const chome = (cno) => {
            router.push({ name: "CHomeVue", query: { cno: state.cno } })
        }
        
        return { state, changeheart, unlike, chome,handleClick }
    },
}
</script>

<style lang="scss" scoped>

</style>