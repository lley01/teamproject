<template>
<div>
<CHHeaderVue style="height: 150px;"></CHHeaderVue>
    <v-app>
        <v-main style="padding: 10px;">
            <v-row dense>
                <v-col sm="2"></v-col>

                <v-col sm="8">
                    <v-row dense="" style="border-bottom: 1px solid #CCC;">
                        <v-col sm="6">
                            <h5><router-link :to="{ name: 'CHomeVue', query: { cno: state.cno } }">홈</router-link> > 
                            <router-link :to="{ name: 'CBoardListVue', query: { cno: state.cno } }">{{state.boardname}}</router-link> > 글쓰기</h5>
                        </v-col>                                
                    </v-row>

                    <v-row dense style="padding-top: 15px; padding-bottom: 10px; padding-left:10px; border-bottom: 1px solid #CCC;">
                        <v-col>
                            <h2>글쓰기</h2>
                        </v-col>
                    </v-row>

                    <v-row dense>
                        <v-col class="col_center">                        
                            <v-card style="width:100%; margin: 10px; margin-top: 20px; margin-bottom: 30px;">
                                <v-expansion-panels style="width:100%">
                                    <v-form v-model="state.valid" style="width:100%">

                                        <!-- 제목 -->
                                        <v-expansion-panel class="panel">
                                            <v-row dense style="padding:10px;">
                                                <v-col sm="2" style="justify-content: right; display: flex; align-items: center; ">
                                                    제목:
                                                </v-col>

                                                <v-col sm="8" style="display: flex; align-items: center; width:100%;">
                                                    <input type="text" v-model="state.title" style="outline-width: 0; padding-left: 3px; width: 100%; border-bottom: 1px solid #CCC;"/>
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
                                                    <ckeditor :editor="state.editor" v-model="state.editorData" @ready="onReady"></ckeditor>
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
                                                    <img :src="state.imageUrl" style="width:200px;" />
                                                    <input type="file" @change="insertFile($event)">
                                                </v-col>

                                                <v-col sm="2"></v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 글쓰기버튼 -->
                                        <v-expansion-panel class="panel">

                                            <v-row dense style="padding:10px; ">
                                                <v-col sm="4"></v-col>

                                                <v-col sm="4" style="justify-content: center; display: flex; align-items: center;">
                                                    <v-btn @click="write" style="width: 100px; height:40px; background-color: gold;">
                                                        <h3>글쓰기</h3>
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
import axios from 'axios';
import FooterVue     from '../../FooterVue.vue';
import CHHeaderVue  from '../CHHeaderVue.vue';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import UploadAdapter from '../../UploadAdapter.js';
import CKEditor      from '@ckeditor/ckeditor5-vue'
import { reactive }  from '@vue/reactivity';
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from '@vue/runtime-core';


export default {
    components: { CHHeaderVue, FooterVue, ckeditor: CKEditor.component },
    setup () {
        const router = useRouter();
        const route  = useRoute();

        const state = reactive({
            writer     : '',
            title      : '',
            editor     : ClassicEditor, // ckeditor종류
            editorData : '',
            boardname  : '클럽게시판',
            imageFile  : '',
            imageUrl   : '',
            valid      : '',
            cno        : route.query.cno,
            token      : sessionStorage.getItem("TOKEN")
        })

        const onReady = (editor) => {
            console.log(editor);
            editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
                    return new UploadAdapter(loader);
            };
            
            editor.editing.view.change(writer => {
                writer.setStyle('height', '600px', editor.editing.view.document.getcluver());
            });
            console.log(editor.editing.view);
        }
        
        const handleCancel = async() => {
            if (confirm('정말 취소하시겠습니까?') == true) {
                router.push({ name: "CBoardListVue", query : { page: 1, cno: state.cno } });
            }
        }

        const write = async() => {
            const url     = `/cluver/api/clubboard/insert`;
            const headers = { "Content-Type": "multipart/form-data", token: state.token };
            const body    = new FormData();
            body.append("cbtitle"  , state.title);
            body.append("cbcontent", state.editorData);
            body.append("file"     , state.imageFile);
            body.append("club"     , state.cno);
            const response = await axios.post(url, body, {headers});
            console.log(response.data);

            if(response.data.status === 200) {
                alert("작성되었습니다.");
                router.push({ name: "CBoardListVue", query: { page: 1, cno: state.cno } });
            }
        }

        const insertFile = (e) => {
            if(e.target.files[0]) {
                state.imageUrl = URL.createObjectURL(e.target.files[0]);
                state.imageFile = e.target.files[0];
            }else {
                state.imageUrl = null;
                state.imageFile = null;
            }
        }

        const handleToken = () => {
            if(state.token === null) {
                alert("로그인이 필요한 페이지입니다.");
                router.push({ name: 'LoginVue' });
            }
        }

        onMounted(()=> {
            handleToken();
        })
        
        return { state, onReady, handleCancel, write, insertFile }
    },
}
</script>

<style lang="scss" scoped>

</style>