<template>
<div>
<HeaderVue style="height: 150px;"></HeaderVue>
    <v-app>
        <v-main style="padding: 10px;">      
            <v-row dense>
                <!-- 사이드 -->
                <v-col sm="2"></v-col>
                
                <!-- 메인 -->
                <v-col sm="8">
                    <v-row dense="" style="border-bottom: 1px solid #CCC;">
                        <v-col sm="6">
                            <h5><router-link to="/">홈</router-link> > <router-link to="/login">로그인</router-link> > 아이디 찾기</h5>
                        </v-col>
                    </v-row>

                    <v-row dense style="border-bottom: 1px solid #CCC;">
                        <v-col style="padding-top: 15px; padding-left: 15px;">
                            <h2>아이디 찾기</h2>
                        </v-col>
                    </v-row>
                    <v-row dense style="padding-top: 20px;">
                        <v-col sm="2"></v-col>

                        <v-col>
                            <h2>개인회원</h2>
                        </v-col>

                        <v-col sm="2"></v-col>
                    </v-row>
                    <form>
                        <v-row dense style="padding-top: 10px;">        
                            <v-col sm="2"></v-col>

                            <v-col style="justify-content: right;display: flex; align-items: center; ">
                            <!-- 아이디 -->
                                <v-expansion-panels style="width:100%">
                                    <v-form v-model="state.valid" style="width:100%">
                                        <v-expansion-panel class="panel">
                                            <v-row>
                                                <v-col style="height: 80px;">
                                                    <v-text-field
                                                    label="이름"
                                                    v-model="state.pname"
                                                    variant="plain"
                                                    density="compact"
                                                    required
                                                    ></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 연락처 -->
                                        <v-expansion-panel class="panel">
                                            <v-row>
                                                <v-col style="height: 80px;">
                                                    <v-text-field
                                                    label="연락처"
                                                    v-model="state.pphone"
                                                    variant="plain"
                                                    :rules="state.phoneRules"
                                                    hint="숫자만 입력하세요"
                                                    density="compact"
                                                    required
                                                    ></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-expansion-panel>
                                    </v-form>
                                </v-expansion-panels>
                            </v-col>

                            <v-col sm="2"></v-col>
                        </v-row>
                        <v-row dense style="padding-bottom: 20px;">
                            <v-col sm="2"></v-col>

                            <v-col style="padding-bottom: 20px; border-bottom: 1px solid #CCC;">
                                <v-btn @click="notyet()" style="width:100%; height:80px; background-color: #ffcc00;">
                                    <h2>아이디찾기</h2>
                                </v-btn>
                            </v-col>

                            <v-col sm="2"></v-col>
                        </v-row>
                    </form>

                    <!-- 기업회원 -->
                    <v-row dense>
                        <v-col sm="2"></v-col>

                        <v-col>
                            <h2>기업회원</h2>
                        </v-col>

                        <v-col sm="2"></v-col>
                    </v-row>
                    <form>
                        <v-row dense style="padding-top: 10px;">        
                            <v-col sm="2"></v-col>

                            <v-col style="justify-content: right;display: flex; align-items: center; ">
                            <!-- 아이디 -->
                                <v-expansion-panels style="width:100%">
                                    <v-form v-model="state.valid" style="width:100%">
                                        <v-expansion-panel class="panel">
                                            <v-row>
                                                <v-col style="height: 80px;">
                                                    <v-text-field
                                                    label="담당자이름"
                                                    v-model="state.cname"
                                                    variant="plain"
                                                    density="compact"
                                                    required
                                                    ></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 연락처 -->
                                        <v-expansion-panel class="panel">
                                            <v-row>
                                                <v-col style="height: 80px;">
                                                    <v-text-field
                                                    label="연락처"
                                                    v-model="state.cphone"
                                                    variant="plain"
                                                    :rules="state.phoneRules"
                                                    hint="숫자만 입력하세요"
                                                    density="compact"
                                                    required
                                                    ></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-expansion-panel>

                                        <!-- 사업자번호 -->
                                        <v-expansion-panel class="panel">
                                            <v-row>
                                                <v-col style="height: 80px;">
                                                    <v-text-field
                                                    label="사업자등록번호"
                                                    v-model="state.cnumber"
                                                    variant="plain"
                                                    hint="10자리 숫자만 입력하세요"                      
                                                    :rules="state.numberRules"
                                                    :counter="10"                      
                                                    density="compact"
                                                    required
                                                    ></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </v-expansion-panel>
                                    </v-form>
                                </v-expansion-panels>
                            </v-col>

                            <v-col sm="2"></v-col>
                        </v-row>
                        <v-row dense style="padding-bottom: 20px; border-bottom: 1px solid #CCC;">
                            <v-col sm="2"></v-col>

                            <v-col>
                                <v-btn @click="notyet()" style="width:100%; height:80px; background-color: #5d00ff;">
                                    <h2 style="color: white">아이디찾기</h2>
                                </v-btn>
                            </v-col>

                            <v-col sm="2"></v-col>
                        </v-row>
                    </form>
                </v-col>

                <!-- 사이드 -->
                <v-col sm="2"></v-col>
            </v-row>
        </v-main>
    </v-app>
    <FooterVue></FooterVue>
</div>
</template>

<script>
import { reactive } from '@vue/reactivity';
import FooterVue from '../../components/FooterVue.vue';
import HeaderVue from '../HeaderVue.vue';

export default {
    components: { HeaderVue, FooterVue },
    setup () {
        const state = reactive({
            pname  : '',
            pphone : '',
            cname  : '',
            cnumber: '',
            chone  : '',            
            numberRules: [
                v => !!v || '필수 입력 사항입니다',
                v => v.length >= 10 || '10자리 숫자만 입력하세요',
                v => v.length <= 10 || '10자리 숫자만 입력하세요',
            ],
            valid  : '',
        })

        const notyet = () => {
            alert('준비중입니다');
        }

        return { state, notyet }
    },
}
</script>

<style lang="scss" scoped>

</style>