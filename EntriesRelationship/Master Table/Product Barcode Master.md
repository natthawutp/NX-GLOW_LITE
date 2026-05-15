# GWH_TM_PBAR
#Entity #Standard #MASTER 

| COLUMN_NAME    | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                    |
|----------------|---------------------|----------|--------------|-----------|---------------------------------------------|
| DEL_FLG        | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                       |
| CRT_YMD        | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                |
| CRT_TIM        | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)               |
| CRT_TMID       | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                     |
| CRT_USER       | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                    |
| CRT_PGM        | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID               |
| CRT_TM_ZONE    | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン         |
| CRT_YMDHMS     | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）    |
| CRT_L_YMDHMS   | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| UPD_YMD        | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                |
| UPD_TIM        | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)               |
| UPD_TMID       | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                 |
| UPD_USER       | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                    |
| UPD_PGM        | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID               |
| UPD_TM_ZONE    | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン         |
| UPD_YMDHMS     | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）    |
| UPD_L_YMDHMS   | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| PBAR_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                     |
| PBAR_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                      |
| PBAR_CUST_COD  | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                       |
| PBAR_FIL       | NVARCHAR2(50 CHAR)  | No       |              | 21        | Barcode Value / バーコード値                      |
| PBAR_PROD_COD  | NVARCHAR2(50 CHAR)  | No       |              | 22        | Product Code / 商品コード                        |
| PBAR_RMKS      | NVARCHAR2(20 CHAR)  | Yes      |              | 23        | Barcode Remarks / バーコード値備考                  |
| PBAR_SPTP_KND  | NVARCHAR2(2 CHAR)   | Yes      |              | 24        | Shape Type / 荷姿区分                           |
| PBAR_CTUP_QTY  | NUMBER(9,0)         | Yes      |              | 25        | Count-up Qty. / 換算数                         |
| PBAR_CFG1_FLG  | CHAR(1 BYTE)        | No       | N            | 26        | Customized Flag 01 / 顧客専用フラグ01              |
| PBAR_CFG2_FLG  | CHAR(1 BYTE)        | No       | N            | 27        | Customized Flag 02 / 顧客専用フラグ02              |
| PBAR_CFG3_FLG  | CHAR(1 BYTE)        | No       | N            | 28        | Customized Flag 03 / 顧客専用フラグ03              |
| PBAR_CFG4_FLG  | CHAR(1 BYTE)        | No       | N            | 29        | Customized Flag 04 / 顧客専用フラグ04              |
| PBAR_CFG5_FLG  | CHAR(1 BYTE)        | No       | N            | 30        | Customized Flag 05 / 顧客専用フラグ05              |
| PBAR_CFG6_FLG  | CHAR(1 BYTE)        | No       | N            | 31        | Customized Flag 06 / 顧客専用フラグ06              |
| PBAR_CFG7_FLG  | CHAR(1 BYTE)        | No       | N            | 32        | Customized Flag 07 / 顧客専用フラグ07              |
| PBAR_CFG8_FLG  | CHAR(1 BYTE)        | No       | N            | 33        | Customized Flag 08 / 顧客専用フラグ08              |
| PBAR_CFG9_FLG  | CHAR(1 BYTE)        | No       | N            | 34        | Customized Flag 09 / 顧客専用フラグ09              |
| PBAR_CFG10_FLG | CHAR(1 BYTE)        | No       | N            | 35        | Customized Flag 10 / 顧客専用フラグ10              |
| PBAR_CFG11_FLG | CHAR(1 BYTE)        | No       | N            | 36        | Customized Flag 11 / 顧客専用フラグ11              |
| PBAR_CFG12_FLG | CHAR(1 BYTE)        | No       | N            | 37        | Customized Flag 12 / 顧客専用フラグ12              |
| PBAR_CFG13_FLG | CHAR(1 BYTE)        | No       | N            | 38        | Customized Flag 13 / 顧客専用フラグ13              |
| PBAR_CFG14_FLG | CHAR(1 BYTE)        | No       | N            | 39        | Customized Flag 14 / 顧客専用フラグ14              |
| PBAR_CFG15_FLG | CHAR(1 BYTE)        | No       | N            | 40        | Customized Flag 15 / 顧客専用フラグ15              |
| PBAR_CNI1_NUM  | NUMBER(9,0)         | Yes      |              | 41        | Customized Number Item 01 / 顧客専用数字項目01      |
| PBAR_CNI2_NUM  | NUMBER(9,0)         | Yes      |              | 42        | Customized Number Item 02 / 顧客専用数字項目02      |
| PBAR_CNI3_NUM  | NUMBER(9,0)         | Yes      |              | 43        | Customized Number Item 03 / 顧客専用数字項目03      |
| PBAR_CNI4_NUM  | NUMBER(9,0)         | Yes      |              | 44        | Customized Number Item 04 / 顧客専用数字項目04      |
| PBAR_CNI5_NUM  | NUMBER(9,0)         | Yes      |              | 45        | Customized Number Item 05 / 顧客専用数字項目05      |
| PBAR_CNI6_NUM  | NUMBER(9,0)         | Yes      |              | 46        | Customized Number Item 06 / 顧客専用数字項目06      |
| PBAR_CNI7_NUM  | NUMBER(9,0)         | Yes      |              | 47        | Customized Number Item 07 / 顧客専用数字項目07      |
| PBAR_CNI8_NUM  | NUMBER(9,0)         | Yes      |              | 48        | Customized Number Item 08 / 顧客専用数字項目08      |
| PBAR_CNI9_NUM  | NUMBER(9,0)         | Yes      |              | 49        | Customized Number Item 09 / 顧客専用数字項目09      |
| PBAR_CNI10_NUM | NUMBER(14,4)        | Yes      |              | 50        | Customized Number Item 10 / 顧客専用数字項目10      |
| PBAR_CNI11_NUM | NUMBER(14,4)        | Yes      |              | 51        | Customized Number Item 11 / 顧客専用数字項目11      |
| PBAR_CNI12_NUM | NUMBER(14,4)        | Yes      |              | 52        | Customized Number Item 12 / 顧客専用数字項目12      |
| PBAR_CNI13_NUM | NUMBER(14,4)        | Yes      |              | 53        | Customized Number Item 13 / 顧客専用数字項目13      |
| PBAR_CNI14_NUM | NUMBER(14,4)        | Yes      |              | 54        | Customized Number Item 14 / 顧客専用数字項目14      |
| PBAR_CNI15_NUM | NUMBER(14,4)        | Yes      |              | 55        | Customized Number Item 15 / 顧客専用数字項目15      |
| PBAR_CIT1      | NVARCHAR2(100 CHAR) | Yes      |              | 56        | Customized Item 01 / 顧客専用項目01               |
| PBAR_CIT2      | NVARCHAR2(100 CHAR) | Yes      |              | 57        | Customized Item 02 / 顧客専用項目02               |
| PBAR_CIT3      | NVARCHAR2(100 CHAR) | Yes      |              | 58        | Customized Item 03 / 顧客専用項目03               |
| PBAR_CIT4      | NVARCHAR2(100 CHAR) | Yes      |              | 59        | Customized Item 04 / 顧客専用項目04               |
| PBAR_CIT5      | NVARCHAR2(100 CHAR) | Yes      |              | 60        | Customized Item 05 / 顧客専用項目05               |
| PBAR_CIT6      | NVARCHAR2(100 CHAR) | Yes      |              | 61        | Customized Item 06 / 顧客専用項目06               |
| PBAR_CIT7      | NVARCHAR2(100 CHAR) | Yes      |              | 62        | Customized Item 07 / 顧客専用項目07               |
| PBAR_CIT8      | NVARCHAR2(100 CHAR) | Yes      |              | 63        | Customized Item 08 / 顧客専用項目08               |
| PBAR_CIT9      | NVARCHAR2(100 CHAR) | Yes      |              | 64        | Customized Item 09 / 顧客専用項目09               |
| PBAR_CIT10     | NVARCHAR2(100 CHAR) | Yes      |              | 65        | Customized Item 10 / 顧客専用項目10               |
| PBAR_CIT11     | NVARCHAR2(100 CHAR) | Yes      |              | 66        | Customized Item 11 / 顧客専用項目11               |
| PBAR_CIT12     | NVARCHAR2(100 CHAR) | Yes      |              | 67        | Customized Item 12 / 顧客専用項目12               |
| PBAR_CIT13     | NVARCHAR2(100 CHAR) | Yes      |              | 68        | Customized Item 13 / 顧客専用項目13               |
| PBAR_CIT14     | NVARCHAR2(100 CHAR) | Yes      |              | 69        | Customized Item 14 / 顧客専用項目14               |
| PBAR_CIT15     | NVARCHAR2(100 CHAR) | Yes      |              | 70        | Customized Item 15 / 顧客専用項目15               |
