#Entity #Standard #MASTER 

| COLUMN_NAME   | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                               |
|---------------|---------------------|----------|--------------|-----------|--------------------------------------------------------|
| DEL_FLG       | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                  |
| CRT_YMD       | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                           |
| CRT_TIM       | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                          |
| CRT_TMID      | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                |
| CRT_USER      | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                               |
| CRT_PGM       | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                          |
| CRT_TM_ZONE   | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                    |
| CRT_YMDHMS    | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）               |
| CRT_L_YMDHMS  | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）            |
| UPD_YMD       | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                           |
| UPD_TIM       | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                          |
| UPD_TMID      | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                            |
| UPD_USER      | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                               |
| UPD_PGM       | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                          |
| UPD_TM_ZONE   | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                    |
| UPD_YMDHMS    | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）               |
| UPD_L_YMDHMS  | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）            |
| LOC_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                |
| LOC_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                 |
| LOC_COD       | NVARCHAR2(18 CHAR)  | No       |              | 20        | Location / ロケーション                                      |
| LOC_AREA_COD  | CHAR(3 BYTE)        | No       |              | 21        | Area Code / エリア                                        |
| LOC_RACK_COD  | NVARCHAR2(10 CHAR)  | Yes      |              | 22        | Rack Code / ラック                                        |
| LOC_PSTN_COD  | NVARCHAR2(3 CHAR)   | Yes      |              | 23        | Position Code / ポジション                                  |
| LOC_LVL_COD   | NVARCHAR2(2 CHAR)   | Yes      |              | 24        | Level Code / レベル                                       |
| LOC_ZONE_COD  | NVARCHAR2(2 CHAR)   | Yes      |              | 25        | Zone Code / ゾーン                                        |
| LOC_CNPS_KND  | CHAR(1 BYTE)        | No       |              | 26        | Control Packing Style Kind / 管理形態区分                    |
| LOC_NOAL_FLG  | CHAR(1 BYTE)        | No       |              | 27        | Allocation Prohibition Flag / 引当禁止フラグ                  |
| LOC_NORP_FLG  | CHAR(1 BYTE)        | No       |              | 28        | Replenishument Prohibition Flag / 補充禁止フラグ              |
| LOC_NOPD_FLG  | CHAR(1 BYTE)        | No       |              | 29        | Identical Product Mixed Flag / 同一商品禁止フラグ               |
| LOC_NOPK_FLG  | CHAR(1 BYTE)        | No       |              | 30        | Identical Product Pick-Key Mixed Flag / 同一商品ピックキー禁止フラグ |
| LOC_ALPR      | NUMBER(9,0)         | No       |              | 31        | Allocation Priority / 引当優先順                            |
| LOC_PKLN      | NUMBER(9,0)         | Yes      |              | 32        | Picking Line Of Flow / ピッキング動線                         |
| LOC_LEN       | NUMBER(12,6)        | Yes      |              | 33        | Length (Depth) / 奥行き                                   |
| LOC_WID       | NUMBER(12,6)        | Yes      |              | 34        | Wide / 間口                                              |
| LOC_HIG       | NUMBER(12,6)        | Yes      |              | 35        | Height / 高さ                                            |
| LOC_RMKS      | NVARCHAR2(50 CHAR)  | Yes      |              | 36        | Location Remarks / ロケーション備考                            |
| LOC_CFG1_FLG  | CHAR(1 BYTE)        | No       | N            | 37        | Customized Flag 01 / 顧客専用フラグ01                         |
| LOC_CFG2_FLG  | CHAR(1 BYTE)        | No       | N            | 38        | Customized Flag 02 / 顧客専用フラグ02                         |
| LOC_CFG3_FLG  | CHAR(1 BYTE)        | No       | N            | 39        | Customized Flag 03 / 顧客専用フラグ03                         |
| LOC_CFG4_FLG  | CHAR(1 BYTE)        | No       | N            | 40        | Customized Flag 04 / 顧客専用フラグ04                         |
| LOC_CFG5_FLG  | CHAR(1 BYTE)        | No       | N            | 41        | Customized Flag 05 / 顧客専用フラグ05                         |
| LOC_CFG6_FLG  | CHAR(1 BYTE)        | No       | N            | 42        | Customized Flag 06 / 顧客専用フラグ06                         |
| LOC_CFG7_FLG  | CHAR(1 BYTE)        | No       | N            | 43        | Customized Flag 07 / 顧客専用フラグ07                         |
| LOC_CFG8_FLG  | CHAR(1 BYTE)        | No       | N            | 44        | Customized Flag 08 / 顧客専用フラグ08                         |
| LOC_CFG9_FLG  | CHAR(1 BYTE)        | No       | N            | 45        | Customized Flag 09 / 顧客専用フラグ09                         |
| LOC_CFG10_FLG | CHAR(1 BYTE)        | No       | N            | 46        | Customized Flag 10 / 顧客専用フラグ10                         |
| LOC_CFG11_FLG | CHAR(1 BYTE)        | No       | N            | 47        | Customized Flag 11 / 顧客専用フラグ11                         |
| LOC_CFG12_FLG | CHAR(1 BYTE)        | No       | N            | 48        | Customized Flag 12 / 顧客専用フラグ12                         |
| LOC_CFG13_FLG | CHAR(1 BYTE)        | No       | N            | 49        | Customized Flag 13 / 顧客専用フラグ13                         |
| LOC_CFG14_FLG | CHAR(1 BYTE)        | No       | N            | 50        | Customized Flag 14 / 顧客専用フラグ14                         |
| LOC_CFG15_FLG | CHAR(1 BYTE)        | No       | N            | 51        | Customized Flag 15 / 顧客専用フラグ15                         |
| LOC_CNI1_NUM  | NUMBER(9,0)         | Yes      |              | 52        | Customized Number Item 01 / 顧客専用数字項目01                 |
| LOC_CNI2_NUM  | NUMBER(9,0)         | Yes      |              | 53        | Customized Number Item 02 / 顧客専用数字項目02                 |
| LOC_CNI3_NUM  | NUMBER(9,0)         | Yes      |              | 54        | Customized Number Item 03 / 顧客専用数字項目03                 |
| LOC_CNI4_NUM  | NUMBER(9,0)         | Yes      |              | 55        | Customized Number Item 04 / 顧客専用数字項目04                 |
| LOC_CNI5_NUM  | NUMBER(9,0)         | Yes      |              | 56        | Customized Number Item 05 / 顧客専用数字項目05                 |
| LOC_CNI6_NUM  | NUMBER(9,0)         | Yes      |              | 57        | Customized Number Item 06 / 顧客専用数字項目06                 |
| LOC_CNI7_NUM  | NUMBER(9,0)         | Yes      |              | 58        | Customized Number Item 07 / 顧客専用数字項目07                 |
| LOC_CNI8_NUM  | NUMBER(9,0)         | Yes      |              | 59        | Customized Number Item 08 / 顧客専用数字項目08                 |
| LOC_CNI9_NUM  | NUMBER(9,0)         | Yes      |              | 60        | Customized Number Item 09 / 顧客専用数字項目09                 |
| LOC_CNI10_NUM | NUMBER(14,4)        | Yes      |              | 61        | Customized Number Item 10 / 顧客専用数字項目10                 |
| LOC_CNI11_NUM | NUMBER(14,4)        | Yes      |              | 62        | Customized Number Item 11 / 顧客専用数字項目11                 |
| LOC_CNI12_NUM | NUMBER(14,4)        | Yes      |              | 63        | Customized Number Item 12 / 顧客専用数字項目12                 |
| LOC_CNI13_NUM | NUMBER(14,4)        | Yes      |              | 64        | Customized Number Item 13 / 顧客専用数字項目13                 |
| LOC_CNI14_NUM | NUMBER(14,4)        | Yes      |              | 65        | Customized Number Item 14 / 顧客専用数字項目14                 |
| LOC_CNI15_NUM | NUMBER(14,4)        | Yes      |              | 66        | Customized Number Item 15 / 顧客専用数字項目15                 |
| LOC_CIT1      | NVARCHAR2(100 CHAR) | Yes      |              | 67        | Customized Item 01 / 顧客専用項目01                          |
| LOC_CIT2      | NVARCHAR2(100 CHAR) | Yes      |              | 68        | Customized Item 02 / 顧客専用項目02                          |
| LOC_CIT3      | NVARCHAR2(100 CHAR) | Yes      |              | 69        | Customized Item 03 / 顧客専用項目03                          |
| LOC_CIT4      | NVARCHAR2(100 CHAR) | Yes      |              | 70        | Customized Item 04 / 顧客専用項目04                          |
| LOC_CIT5      | NVARCHAR2(100 CHAR) | Yes      |              | 71        | Customized Item 05 / 顧客専用項目05                          |
| LOC_CIT6      | NVARCHAR2(100 CHAR) | Yes      |              | 72        | Customized Item 06 / 顧客専用項目06                          |
| LOC_CIT7      | NVARCHAR2(100 CHAR) | Yes      |              | 73        | Customized Item 07 / 顧客専用項目07                          |
| LOC_CIT8      | NVARCHAR2(100 CHAR) | Yes      |              | 74        | Customized Item 08 / 顧客専用項目08                          |
| LOC_CIT9      | NVARCHAR2(100 CHAR) | Yes      |              | 75        | Customized Item 09 / 顧客専用項目09                          |
| LOC_CIT10     | NVARCHAR2(100 CHAR) | Yes      |              | 76        | Customized Item 10 / 顧客専用項目10                          |
| LOC_CIT11     | NVARCHAR2(100 CHAR) | Yes      |              | 77        | Customized Item 11 / 顧客専用項目11                          |
| LOC_CIT12     | NVARCHAR2(100 CHAR) | Yes      |              | 78        | Customized Item 12 / 顧客専用項目12                          |
| LOC_CIT13     | NVARCHAR2(100 CHAR) | Yes      |              | 79        | Customized Item 13 / 顧客専用項目13                          |
| LOC_CIT14     | NVARCHAR2(100 CHAR) | Yes      |              | 80        | Customized Item 14 / 顧客専用項目14                          |
| LOC_CIT15     | NVARCHAR2(100 CHAR) | Yes      |              | 81        | Customized Item 15 / 顧客専用項目15                          |
| LOC_TEMP_CTR  | CHAR(2 BYTE)        | Yes      |              | 82        | Temperature Control                                    |
| LOC_APRD_COD  | NVARCHAR2(50 CHAR)  | Yes      |              | 83        | Allowed Product Code / 商品コード指定                         |
| LOC_SBIV      | NVARCHAR2(20 CHAR)  | Yes      |              | 84        | Sub Inventry / 等級コード                                   |
