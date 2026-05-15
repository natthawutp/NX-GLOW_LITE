# GWH_TM_AREA
#Entity #Standard #MASTER 

| COLUMN_NAME    | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                               |
|----------------|---------------------|----------|--------------|-----------|--------------------------------------------------------|
| DEL_FLG        | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                  |
| CRT_YMD        | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                           |
| CRT_TIM        | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                          |
| CRT_TMID       | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                |
| CRT_USER       | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                               |
| CRT_PGM        | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                          |
| CRT_TM_ZONE    | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                    |
| CRT_YMDHMS     | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）               |
| CRT_L_YMDHMS   | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）            |
| UPD_YMD        | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                           |
| UPD_TIM        | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                          |
| UPD_TMID       | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                            |
| UPD_USER       | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                               |
| UPD_PGM        | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                          |
| UPD_TM_ZONE    | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                    |
| UPD_YMDHMS     | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）               |
| UPD_L_YMDHMS   | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）            |
| AREA_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                |
| AREA_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                 |
| AREA_COD       | CHAR(3 BYTE)        | No       |              | 20        | Area Code / エリア                                        |
| AREA_NAM       | NVARCHAR2(50 CHAR)  | Yes      |              | 21        | Area Name / エリア名称                                      |
| AREA_SPTP_KND  | NVARCHAR2(2 CHAR)   | Yes      |              | 22        | Shape Type / 荷姿区分                                      |
| AREA_RCAR_KND  | CHAR(2 BYTE)        | Yes      |              | 23        | Recomended Area Kind / 推奨エリア種別                         |
| AREA_PIKG_COD  | NVARCHAR2(3 CHAR)   | Yes      |              | 24        | Picking Group Code / ピッキンググループコード                      |
| AREA_TEMP_KND  | NVARCHAR2(2 CHAR)   | Yes      |              | 25        | Temperature Control Kind / 温度調整区分                      |
| AREA_CNPS_KND  | CHAR(1 BYTE)        | No       |              | 26        | Control Packing Style Kind / 管理形態区分                    |
| AREA_NOAL_FLG  | CHAR(1 BYTE)        | No       |              | 27        | Allocation Prohibition Flag / 引当禁止フラグ                  |
| AREA_NORP_FLG  | CHAR(1 BYTE)        | No       |              | 28        | Replenishment Prohibition Flag / 補充禁止フラグ               |
| AREA_NOPD_FLG  | CHAR(1 BYTE)        | No       |              | 29        | Identical Product Mixed Flag / 同一商品禁止フラグ               |
| AREA_NOPK_FLG  | CHAR(1 BYTE)        | No       |              | 30        | Identical Product Pick-Key Mixed Flag / 同一商品ピックキー禁止フラグ |
| AREA_LOCK_FLG  | CHAR(1 BYTE)        | No       | N            | 31        | Lock Flag / ロックフラグ                                     |
| AREA_CFG1_FLG  | CHAR(1 BYTE)        | No       | N            | 32        | Customized Flag 01 / 顧客専用フラグ01                         |
| AREA_CFG2_FLG  | CHAR(1 BYTE)        | No       | N            | 33        | Customized Flag 02 / 顧客専用フラグ02                         |
| AREA_CFG3_FLG  | CHAR(1 BYTE)        | No       | N            | 34        | Customized Flag 03 / 顧客専用フラグ03                         |
| AREA_CFG4_FLG  | CHAR(1 BYTE)        | No       | N            | 35        | Customized Flag 04 / 顧客専用フラグ04                         |
| AREA_CFG5_FLG  | CHAR(1 BYTE)        | No       | N            | 36        | Customized Flag 05 / 顧客専用フラグ05                         |
| AREA_CFG6_FLG  | CHAR(1 BYTE)        | No       | N            | 37        | Customized Flag 06 / 顧客専用フラグ06                         |
| AREA_CFG7_FLG  | CHAR(1 BYTE)        | No       | N            | 38        | Customized Flag 07 / 顧客専用フラグ07                         |
| AREA_CFG8_FLG  | CHAR(1 BYTE)        | No       | N            | 39        | Customized Flag 08 / 顧客専用フラグ08                         |
| AREA_CFG9_FLG  | CHAR(1 BYTE)        | No       | N            | 40        | Customized Flag 09 / 顧客専用フラグ09                         |
| AREA_CFG10_FLG | CHAR(1 BYTE)        | No       | N            | 41        | Customized Flag 10 / 顧客専用フラグ10                         |
| AREA_CFG11_FLG | CHAR(1 BYTE)        | No       | N            | 42        | Customized Flag 11 / 顧客専用フラグ11                         |
| AREA_CFG12_FLG | CHAR(1 BYTE)        | No       | N            | 43        | Customized Flag 12 / 顧客専用フラグ12                         |
| AREA_CFG13_FLG | CHAR(1 BYTE)        | No       | N            | 44        | Customized Flag 13 / 顧客専用フラグ13                         |
| AREA_CFG14_FLG | CHAR(1 BYTE)        | No       | N            | 45        | Customized Flag 14 / 顧客専用フラグ14                         |
| AREA_CFG15_FLG | CHAR(1 BYTE)        | No       | N            | 46        | Customized Flag 15 / 顧客専用フラグ15                         |
| AREA_CNI1_NUM  | NUMBER(9,0)         | Yes      |              | 47        | Customized Number Item 01 / 顧客専用数字項目01                 |
| AREA_CNI2_NUM  | NUMBER(9,0)         | Yes      |              | 48        | Customized Number Item 02 / 顧客専用数字項目02                 |
| AREA_CNI3_NUM  | NUMBER(9,0)         | Yes      |              | 49        | Customized Number Item 03 / 顧客専用数字項目03                 |
| AREA_CNI4_NUM  | NUMBER(9,0)         | Yes      |              | 50        | Customized Number Item 04 / 顧客専用数字項目04                 |
| AREA_CNI5_NUM  | NUMBER(9,0)         | Yes      |              | 51        | Customized Number Item 05 / 顧客専用数字項目05                 |
| AREA_CNI6_NUM  | NUMBER(9,0)         | Yes      |              | 52        | Customized Number Item 06 / 顧客専用数字項目06                 |
| AREA_CNI7_NUM  | NUMBER(9,0)         | Yes      |              | 53        | Customized Number Item 07 / 顧客専用数字項目07                 |
| AREA_CNI8_NUM  | NUMBER(9,0)         | Yes      |              | 54        | Customized Number Item 08 / 顧客専用数字項目08                 |
| AREA_CNI9_NUM  | NUMBER(9,0)         | Yes      |              | 55        | Customized Number Item 09 / 顧客専用数字項目09                 |
| AREA_CNI10_NUM | NUMBER(14,4)        | Yes      |              | 56        | Customized Number Item 10 / 顧客専用数字項目10                 |
| AREA_CNI11_NUM | NUMBER(14,4)        | Yes      |              | 57        | Customized Number Item 11 / 顧客専用数字項目11                 |
| AREA_CNI12_NUM | NUMBER(14,4)        | Yes      |              | 58        | Customized Number Item 12 / 顧客専用数字項目12                 |
| AREA_CNI13_NUM | NUMBER(14,4)        | Yes      |              | 59        | Customized Number Item 13 / 顧客専用数字項目13                 |
| AREA_CNI14_NUM | NUMBER(14,4)        | Yes      |              | 60        | Customized Number Item 14 / 顧客専用数字項目14                 |
| AREA_CNI15_NUM | NUMBER(14,4)        | Yes      |              | 61        | Customized Number Item 15 / 顧客専用数字項目15                 |
| AREA_CIT1      | NVARCHAR2(100 CHAR) | Yes      |              | 62        | Customized Item 01 / 顧客専用項目01                          |
| AREA_CIT2      | NVARCHAR2(100 CHAR) | Yes      |              | 63        | Customized Item 02 / 顧客専用項目02                          |
| AREA_CIT3      | NVARCHAR2(100 CHAR) | Yes      |              | 64        | Customized Item 03 / 顧客専用項目03                          |
| AREA_CIT4      | NVARCHAR2(100 CHAR) | Yes      |              | 65        | Customized Item 04 / 顧客専用項目04                          |
| AREA_CIT5      | NVARCHAR2(100 CHAR) | Yes      |              | 66        | Customized Item 05 / 顧客専用項目05                          |
| AREA_CIT6      | NVARCHAR2(100 CHAR) | Yes      |              | 67        | Customized Item 06 / 顧客専用項目06                          |
| AREA_CIT7      | NVARCHAR2(100 CHAR) | Yes      |              | 68        | Customized Item 07 / 顧客専用項目07                          |
| AREA_CIT8      | NVARCHAR2(100 CHAR) | Yes      |              | 69        | Customized Item 08 / 顧客専用項目08                          |
| AREA_CIT9      | NVARCHAR2(100 CHAR) | Yes      |              | 70        | Customized Item 09 / 顧客専用項目09                          |
| AREA_CIT10     | NVARCHAR2(100 CHAR) | Yes      |              | 71        | Customized Item 10 / 顧客専用項目10                          |
| AREA_CIT11     | NVARCHAR2(100 CHAR) | Yes      |              | 72        | Customized Item 11 / 顧客専用項目11                          |
| AREA_CIT12     | NVARCHAR2(100 CHAR) | Yes      |              | 73        | Customized Item 12 / 顧客専用項目12                          |
| AREA_CIT13     | NVARCHAR2(100 CHAR) | Yes      |              | 74        | Customized Item 13 / 顧客専用項目13                          |
| AREA_CIT14     | NVARCHAR2(100 CHAR) | Yes      |              | 75        | Customized Item 14 / 顧客専用項目14                          |
| AREA_CIT15     | NVARCHAR2(100 CHAR) | Yes      |              | 76        | Customized Item 15 / 顧客専用項目15                          |
