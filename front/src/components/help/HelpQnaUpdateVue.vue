<template>
<div>
<HeaderVue style="height: 150px;"></HeaderVue>
    <v-app>
        <v-main style="padding: 10px;">
            <v-row dense>
                <v-col sm="2"></v-col>

                <v-col sm="8">
                    <v-row dense="" style="border-bottom: 1px solid #CCC;">
                        <v-col sm="6">
                            <h5><router-link to="/">홈</router-link> > <router-link to="/hqna">질문및답변</router-link> > 글수정</h5>
                        </v-col>                                
                    </v-row>

                    <v-row dense style="padding-top: 15px; padding-bottom: 10px; padding-left:10px; border-bottom: 1px solid #CCC;">
                        <v-col>
                            <h2>글수정</h2> 
                           
                        </v-col>
                    </v-row>

                    <v-row dense>
                        <v-col class="col_center">                        
                            <v-card style="width:100%; margin: 10px; margin-top: 20px; margin-bottom: 30px;">
                                <v-expansion-panels style="width:100%">
                                    <v-form v-model="state.valid" style="width:100%">
                                        {{state.valid}}
                                        <!-- 제목 -->
                                        <v-expansion-panel class="panel">
                                            <v-row dense style="padding:10px;">
                                                <v-col sm="2" style="justify-content: right; display: flex; align-items: center; ">
                                                    제목:
                                                </v-col>

                                                <v-col sm="8" style="display: flex; align-items: center; width:100%;">
                                                    <input type="text" v-model="state.valid.qtitle" style="outline-width: 0; padding-left: 3px; width: 100%; border-bottom: 1px solid #CCC;"/>
                                                </v-col>

                                                <v-col sm="2"></v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 내용 -->
                                        <v-expansion-panel class="panel">
                                            <v-row dense style="padding:10px;">
                                                <v-col sm="2" style="justify-content: right; display: flex; align-items: center; margin-top: 13px;">
                                                    내용:
                                                </v-col>

                                                <v-col sm="8" >
                                                    <ckeditor :editor="state.editor" v-model="state.valid.qcontent" @ready="onReady"></ckeditor>
                                                </v-col>

                                                <v-col sm="2"></v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 파일첨부 -->
                                        <v-expansion-panel class="panel">
                                            <v-row dense style="padding:10px;">
                                                <v-col sm="2" style="justify-content: right; display: flex; align-items: center;">
                                                    파일첨부:
                                                </v-col>

                                                <v-col sm="8" style="display: flex; align-items: center;">
                                                    <input type="file">
                                                </v-col>

                                                <v-col sm="2"></v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 글쓰기버튼 -->
                                        <v-expansion-panel class="panel">
                                            <v-row dense style="padding:10px; ">
                                                <v-col sm="4"></v-col>

                                                <v-col sm="4" style="justify-content: center; display: flex; align-items: center;">
                                                    <v-btn @click="handleUpdate" style="width: 100px; height:40px; background-color: gold;">
                                                        <h3>수정</h3>
                                                    </v-btn>

                                                    <v-btn @click="handleCancel" style="width: 100px; height:40px; margin-left:20px; background-color: white;">
                                                        <h3>취소</h3>
                                                    </v-btn>
                                                </v-col>

                                                <v-col sm="4"></v-col>
                                            </v-row>
                                        </v-expansion-panel>
                                    </v-form>
                                </v-expansion-panels>
                            </v-card>
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
import FooterVue     from '../FooterVue.vue';
import HeaderVue     from '../HeaderVue.vue';
import { onMounted } from '@vue/runtime-core';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import UploadAdapter from '../UploadAdapter.js';
import CKEditor      from '@ckeditor/ckeditor5-vue'
import { reactive }  from '@vue/reactivity';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';


export default {
    components: { HeaderVue, FooterVue, ckeditor: CKEditor.component },
    setup () {

        const route = useRoute();
        const router = useRouter();

        const state = reactive({
            qno        : route.query.qno,
            mid        : '',
            qtitle     : '',
            qcontent   : '',
            qtype      : 1,
            editor     : ClassicEditor, // ckeditor종류
            editorData : '',
            token      : sessionStorage.getItem("TOKEN"),
            valid      : '',
        })

        const handleData = async() => {
            const url      = `/cluver/api/board1/selectone?bno=${state.bno}`;
            const headers  = { "Content-Type": "application/json", "token": state.token };
            const response = await axios.get(url, {headers});
            console.log(response.data);

            if(response.data.status === 200){
                console.log(response.data.result);
                state.valid = response.data.result;
                // console.log(state.item);
            }
        }
        

        const handleUpdate = async() => {
            const url      = `/cluver/api/board1/update`;
            const headers  = { "Content-Type" :"application/json", "token": state.token };
            const body     = new FormData();
            body.append("member"  , state.valid.mid);
            body.append("bno"     , state.valid.bno);
            body.append("btitle"  , state.valid.btitle);
            body.append("bcontent", state.valid.bcontent);
            body.append("btype"   , state.valid.btype);

            const response = await axios.put(url, body, {headers});
            console.log(response.data);
            if(response.data.status === 200){
                alert('수정완료');
                // 원래 게시글로 돌아가야 함
                router.push({ name: 'BoardListVue' });
            }

        }

        const handleCancel = async() => {
            if(confirm('정말 취소하시겠습니까?') == true) {
                router.push({ name: "BoardListVue"});
            }
        }

        const onReady = (editor) => {
            console.log("ckeditor ==> ", editor);
            editor.plugins.get( 'FileRepository' ).createUploadAdapter = (loader) => {
                return new UploadAdapter(loader);
            };
            
            editor.editing.view.change( writer => {
                writer.setStyle('height', '600px', editor.editing.view.document.getcluver());
            });
            console.log(editor.editing.view);
        }


        onMounted( async() => {
            await handleData();
        })

        return { state, onReady, handleCancel, handleUpdate }
    },
}
</script>

<style lang="scss" scoped>

</style>