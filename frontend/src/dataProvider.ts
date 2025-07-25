import { DataProvider } from 'react-admin';
import simpleRestProvider from 'ra-data-simple-rest';

// Spring Boot 백엔드 API 엔드포인트
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// REST API 데이터 프로바이더 생성
export const dataProvider: DataProvider = simpleRestProvider(API_URL); 