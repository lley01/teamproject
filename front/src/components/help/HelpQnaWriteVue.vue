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
                            <h5><router-link to="/">홈</router-link> > <router-link to="/h">고객센터</router-link> > <router-link to="/hqna">질문및답변</router-link> > 문의글쓰기</h5>
                        </v-col>                                
                    </v-row>

                    <v-row dense style="padding-top: 19px; padding-bottom: 11px; padding-left: 10px; border-bottom: 1px solid #CCC;">
                        <v-col class="col_left">
                            <h2>문의글쓰기</h2>
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
                                                    <input type="text" v-model="state.qtitle" style="outline-width: 0; padding-left: 3px; width: 100%; border-bottom: 1px solid #CCC;"/>
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
                                                    <input type="file">
                                                </v-col>

                                                <v-col sm="2"></v-col>

                                                <!-- {{state.qprivate}} -->
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 글쓰기버튼 -->
                                        <v-expansion-panel class="panel">

                                            <v-row dense style="padding:10px; ">
                                                <v-col sm="4"></v-col>

                                                <v-col sm="4" style="justify-content: center; display: flex; align-items: center;">
                                                    <v-btn @click="handleInsert" style="width: 100px; height:40px; background-color: gold;">
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
import FooterVue     from '../FooterVue.vue';
import HeaderVue     from '../HeaderVue.vue';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import UploadAdapter from '../UploadAdapter.js';
import CKEditor      from '@ckeditor/ckeditor5-vue'
import { reactive }  from '@vue/reactivity';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
    components: { HeaderVue, FooterVue, ckeditor: CKEditor.component },
    setup () {
        const router = useRouter();

        const state = reactive({
            mid        : '',
            qtitle      : '',
            editor     : ClassicEditor, // ckeditor종류
            editorData : '',
            valid      : '',
            token      : sessionStorage.getItem("TOKEN"),
            qprivate : 'n',
        })

        const onReady = ( editor ) => {
            console.log(editor);
            editor.plugins.get( 'FileRepository' ).createUploadAdapter = ( loader ) => {
                    return new UploadAdapter( loader );
            };
            
            editor.editing.view.change( writer => {
                writer.setStyle( 'height', '600px', editor.editing.view.document.getcluver() );
            });
            console.log(editor.editing.view);
        }
        
        const handleCancel = async() => {
            if (confirm('정말 취소하시겠습니까?') == true) {
                router.push({ name: "BoardListVue"});
            }
        }

        const handleInsert = async() => {
            const url = `/cluver/api/qna/insert`;
            const headers = {
                "Content-Type" : "application/json",
                "token"        : state.token,
            };
            
            const body= new FormData();
            body.append("mid", state.mid);
            body.append("qtitle", state.qtitle);
            body.append("qcontent", state.editorData);
            body.append("qprivate", state.qprivate);
           
            const response = await axios.post(url, body, {headers});
            console.log(response.data);
            if(response.data.status === 200){
                alert('등록완료');
                router.push({name: 'HelpQnaVue'});
            }

        }

        return { state, onReady, handleCancel, handleInsert }
    },
}
</script>

<style lang="scss" scoped>

</style>