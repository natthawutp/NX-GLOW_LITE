# GWH_TJ_TR
#Entity #Standard #LOCATION 

| DEL_FLG      | CHAR(1 BYTE)        | No  |   | 1  | Delete Flag / 論理削除フラグ                       |
|--------------|---------------------|-----|---|----|---------------------------------------------|
| CRT_YMD      | CHAR(8 BYTE)        | No  |   | 2  | Create Date (UTC) / 作成日(標準時)                |
| CRT_TIM      | CHAR(6 BYTE)        | No  |   | 3  | Create HMS (UTC) / 作成時分秒(標準時)               |
| CRT_TMID     | NVARCHAR2(100 CHAR) | No  |   | 4  | Create Term Id / 作成端末ID                     |
| CRT_USER     | NVARCHAR2(50 CHAR)  | No  |   | 5  | Create User Id / 作成ユーザID                    |
| CRT_PGM      | NVARCHAR2(30 CHAR)  | No  |   | 6  | Create Program Id / 作成プログラムID               |
| CRT_TM_ZONE  | NVARCHAR2(3 CHAR)   | No  |   | 7  | Create User Time Zone / 作成ユーザタイムゾーン         |
| CRT_YMDHMS   | TIMESTAMP(6)        | No  |   | 8  | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）    |
| CRT_L_YMDHMS | TIMESTAMP(6)        | No  |   | 9  | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| UPD_YMD      | CHAR(8 BYTE)        | No  |   | 10 | Update Date (UTC) / 更新日(標準時)                |
| UPD_TIM      | CHAR(6 BYTE)        | No  |   | 11 | Update HMS (UTC) / 更新時分秒(標準時)               |
| UPD_TMID     | NVARCHAR2(100 CHAR) | No  |   | 12 | Update Terminal ID / 更新端末ID                 |
| UPD_USER     | NVARCHAR2(50 CHAR)  | No  |   | 13 | Update User ID / 更新ユーザID                    |
| UPD_PGM      | NVARCHAR2(30 CHAR)  | No  |   | 14 | Update Program ID / 更新プログラムID               |
| UPD_TM_ZONE  | NVARCHAR2(3 CHAR)   | No  |   | 15 | Update User Time Zone / 更新ユーザタイムゾーン         |
| UPD_YMDHMS   | TIMESTAMP(6)        | No  |   | 16 | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）    |
| UPD_L_YMDHMS | TIMESTAMP(6)        | No  |   | 17 | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| TR_CPNY_COD  | NVARCHAR2(6 CHAR)   | No  |   | 18 | Company Code / カンパニーコード                     |
| TR_WHS_COD   | NVARCHAR2(4 CHAR)   | No  |   | 19 | Warehouse Code / 倉庫コード                      |
| TR_CUST_COD  | NVARCHAR2(13 CHAR)  | No  |   | 20 | Customer Code / 顧客コード                       |
| TR_OP_NUM    | NVARCHAR2(6 CHAR)   | No  |   | 21 | Operating Number / 処理番号                     |
| TR_OPLN_NUM  | NUMBER(4,0)         | No  |   | 22 | Operating Line Number / 処理ラインNo             |
| TR_OPSQ_NUM  | NUMBER(4,0)         | No  |   | 23 | Operating Seq Number / 処理SEQ                |
| TR_AV_NUM    | NVARCHAR2(6 CHAR)   | No  |   | 24 | Arrival Number / 入荷番号                       |
| TR_AVLN_NUM  | NUMBER(4,0)         | No  |   | 25 | Arrival Line No / 入荷ラインNo                   |
| TR_AVSQ_NUM  | NUMBER(4,0)         | No  |   | 26 | Arrival Seq No / 入荷SEQNo                    |
| TR_TRN_KND   | NVARCHAR2(3 CHAR)   | No  |   | 27 | Transaction Kind / 伝票区分                     |
| TR_OP_YMD    | DATE                | No  |   | 28 | Operating Date / 処理日                        |
| TR_PROD_COD  | NVARCHAR2(50 CHAR)  | No  |   | 29 | Product Code / 商品コード                        |
| TR_ORGN_COD  | CHAR(2 BYTE)        | Yes |   | 30 | Country Of Origin / 原産国                     |
| TR_PPCS_QTY  | NUMBER(9,0)         | No  |   | 31 | Number Of Pieces Per Case / ケース入数           |
| TR_RCS_QTY   | NUMBER(9,0)         | No  |   | 32 | Case QTY(Result.) / ケース実績数                  |
| TR_RPC_QTY   | NUMBER(12,3)        | No  |   | 33 | Piece QTY(Result.) / バラ実績数                  |
| TR_RTPC_QTY  | NUMBER(12,3)        | No  |   | 34 | Total Piece QTY(Result.) / 総バラ実績数           |
| TR_AREA_COD  | CHAR(3 BYTE)        | Yes |   | 35 | Area / エリア                                  |
| TR_RACK_COD  | NVARCHAR2(10 CHAR)  | Yes |   | 36 | Rack / ラック                                  |
| TR_PSTN_COD  | NVARCHAR2(3 CHAR)   | Yes |   | 37 | Position / ポジション                            |
| TR_LVL_COD   | NVARCHAR2(2 CHAR)   | Yes |   | 38 | Level / レベル                                 |
| TR_SBIV_COD  | NVARCHAR2(20 CHAR)  | Yes |   | 39 | Sub Inventry / 等級コード                        |
| TR_PIK1      | NVARCHAR2(30 CHAR)  | Yes |   | 40 | PICKING KEY1 / ピッキングキー1                     |
| TR_PIK2      | NVARCHAR2(30 CHAR)  | Yes |   | 41 | PICKING KEY2 / ピッキングキー2                     |
| TR_PIK3      | NVARCHAR2(30 CHAR)  | Yes |   | 42 | PICKING KEY3 / ピッキングキー3                     |
| TR_PIK4      | NVARCHAR2(30 CHAR)  | Yes |   | 43 | PICKING KEY4 / ピッキングキー4                     |
| TR_PIK5      | NVARCHAR2(30 CHAR)  | Yes |   | 44 | PICKING KEY5 / ピッキングキー5                     |
| TR_PIK6      | NVARCHAR2(30 CHAR)  | Yes |   | 45 | PICKING KEY6 / ピッキングキー6                     |
| TR_PIK7      | NVARCHAR2(30 CHAR)  | Yes |   | 46 | PICKING KEY7 / ピッキングキー7                     |
| TR_DMG_FLG   | CHAR(1 BYTE)        | No  |   | 47 | Damage/Hold Flag / ダメージ/ホールドフラグ             |
| TR_RSN_COD   | NVARCHAR2(3 CHAR)   | Yes |   | 48 | Reason Code / 理由コード                         |
| TR_TRNS_RMKS | NVARCHAR2(500 CHAR) | Yes |   | 49 | Remarks for Transfer / 振替備考                 |
| TR_PPB_QTY   | NUMBER(9,0)         | Yes |   | 50 | Number Of Pieces Per Ball / ボール入数           |
| TR_RBL_QTY   | NUMBER(9,0)         | No  | 0 | 51 | Ball QTY(Result.) / ボール実績数                  |
| TR_CFG1_FLG  | CHAR(1 BYTE)        | No  | N | 52 | Customized Flag 01 / 顧客専用フラグ01              |
| TR_CFG2_FLG  | CHAR(1 BYTE)        | No  | N | 53 | Customized Flag 02 / 顧客専用フラグ02              |
| TR_CFG3_FLG  | CHAR(1 BYTE)        | No  | N | 54 | Customized Flag 03 / 顧客専用フラグ03              |
| TR_CFG4_FLG  | CHAR(1 BYTE)        | No  | N | 55 | Customized Flag 04 / 顧客専用フラグ04              |
| TR_CFG5_FLG  | CHAR(1 BYTE)        | No  | N | 56 | Customized Flag 05 / 顧客専用フラグ05              |
| TR_CFG6_FLG  | CHAR(1 BYTE)        | No  | N | 57 | Customized Flag 06 / 顧客専用フラグ06              |
| TR_CFG7_FLG  | CHAR(1 BYTE)        | No  | N | 58 | Customized Flag 07 / 顧客専用フラグ07              |
| TR_CFG8_FLG  | CHAR(1 BYTE)        | No  | N | 59 | Customized Flag 08 / 顧客専用フラグ08              |
| TR_CFG9_FLG  | CHAR(1 BYTE)        | No  | N | 60 | Customized Flag 09 / 顧客専用フラグ09              |
| TR_CFG10_FLG | CHAR(1 BYTE)        | No  | N | 61 | Customized Flag 10 / 顧客専用フラグ10              |
| TR_CFG11_FLG | CHAR(1 BYTE)        | No  | N | 62 | Customized Flag 11 / 顧客専用フラグ11              |
| TR_CFG12_FLG | CHAR(1 BYTE)        | No  | N | 63 | Customized Flag 12 / 顧客専用フラグ12              |
| TR_CFG13_FLG | CHAR(1 BYTE)        | No  | N | 64 | Customized Flag 13 / 顧客専用フラグ13              |
| TR_CFG14_FLG | CHAR(1 BYTE)        | No  | N | 65 | Customized Flag 14 / 顧客専用フラグ14              |
| TR_CFG15_FLG | CHAR(1 BYTE)        | No  | N | 66 | Customized Flag 15 / 顧客専用フラグ15              |
| TR_CNI1_NUM  | NUMBER(9,0)         | Yes |   | 67 | Customized Number Item 01 / 顧客専用数字項目01      |
| TR_CNI2_NUM  | NUMBER(9,0)         | Yes |   | 68 | Customized Number Item 02 / 顧客専用数字項目02      |
| TR_CNI3_NUM  | NUMBER(9,0)         | Yes |   | 69 | Customized Number Item 03 / 顧客専用数字項目03      |
| TR_CNI4_NUM  | NUMBER(9,0)         | Yes |   | 70 | Customized Number Item 04 / 顧客専用数字項目04      |
| TR_CNI5_NUM  | NUMBER(9,0)         | Yes |   | 71 | Customized Number Item 05 / 顧客専用数字項目05      |
| TR_CNI6_NUM  | NUMBER(9,0)         | Yes |   | 72 | Customized Number Item 06 / 顧客専用数字項目06      |
| TR_CNI7_NUM  | NUMBER(9,0)         | Yes |   | 73 | Customized Number Item 07 / 顧客専用数字項目07      |
| TR_CNI8_NUM  | NUMBER(9,0)         | Yes |   | 74 | Customized Number Item 08 / 顧客専用数字項目08      |
| TR_CNI9_NUM  | NUMBER(9,0)         | Yes |   | 75 | Customized Number Item 09 / 顧客専用数字項目09      |
| TR_CNI10_NUM | NUMBER(14,4)        | Yes |   | 76 | Customized Number Item 10 / 顧客専用数字項目10      |
| TR_CNI11_NUM | NUMBER(14,4)        | Yes |   | 77 | Customized Number Item 11 / 顧客専用数字項目11      |
| TR_CNI12_NUM | NUMBER(14,4)        | Yes |   | 78 | Customized Number Item 12 / 顧客専用数字項目12      |
| TR_CNI13_NUM | NUMBER(14,4)        | Yes |   | 79 | Customized Number Item 13 / 顧客専用数字項目13      |
| TR_CNI14_NUM | NUMBER(14,4)        | Yes |   | 80 | Customized Number Item 14 / 顧客専用数字項目14      |
| TR_CNI15_NUM | NUMBER(14,4)        | Yes |   | 81 | Customized Number Item 15 / 顧客専用数字項目15      |
| TR_CIT1      | NVARCHAR2(100 CHAR) | Yes |   | 82 | Customized Item 01 / 顧客専用項目01               |
| TR_CIT2      | NVARCHAR2(100 CHAR) | Yes |   | 83 | Customized Item 02 / 顧客専用項目02               |
| TR_CIT3      | NVARCHAR2(100 CHAR) | Yes |   | 84 | Customized Item 03 / 顧客専用項目03               |
| TR_CIT4      | NVARCHAR2(100 CHAR) | Yes |   | 85 | Customized Item 04 / 顧客専用項目04               |
| TR_CIT5      | NVARCHAR2(100 CHAR) | Yes |   | 86 | Customized Item 05 / 顧客専用項目05               |
| TR_CIT6      | NVARCHAR2(100 CHAR) | Yes |   | 87 | Customized Item 06 / 顧客専用項目06               |
| TR_CIT7      | NVARCHAR2(100 CHAR) | Yes |   | 88 | Customized Item 07 / 顧客専用項目07               |
| TR_CIT8      | NVARCHAR2(100 CHAR) | Yes |   | 89 | Customized Item 08 / 顧客専用項目08               |
| TR_CIT9      | NVARCHAR2(100 CHAR) | Yes |   | 90 | Customized Item 09 / 顧客専用項目09               |
| TR_CIT10     | NVARCHAR2(100 CHAR) | Yes |   | 91 | Customized Item 10 / 顧客専用項目10               |
| TR_CIT11     | NVARCHAR2(100 CHAR) | Yes |   | 92 | Customized Item 11 / 顧客専用項目11               |
| TR_CIT12     | NVARCHAR2(100 CHAR) | Yes |   | 93 | Customized Item 12 / 顧客専用項目12               |
| TR_CIT13     | NVARCHAR2(100 CHAR) | Yes |   | 94 | Customized Item 13 / 顧客専用項目13               |
| TR_CIT14     | NVARCHAR2(100 CHAR) | Yes |   | 95 | Customized Item 14 / 顧客専用項目14               |
| TR_CIT15     | NVARCHAR2(100 CHAR) | Yes |   | 96 | Customized Item 15 / 顧客専用項目15               |
