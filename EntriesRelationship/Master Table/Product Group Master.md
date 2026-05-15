# GWH_TM_PROD_G
#Entity #Standard #MASTER 

| COLUMN_NAME        | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                               |
|--------------------|---------------------|----------|--------------|-----------|--------------------------------------------------------|
| DEL_FLG            | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                  |
| CRT_YMD            | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                           |
| CRT_TIM            | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                          |
| CRT_TMID           | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                |
| CRT_USER           | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                               |
| CRT_PGM            | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                          |
| CRT_TM_ZONE        | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                    |
| CRT_YMDHMS         | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）               |
| CRT_L_YMDHMS       | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）            |
| UPD_YMD            | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                           |
| UPD_TIM            | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                          |
| UPD_TMID           | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                            |
| UPD_USER           | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                               |
| UPD_PGM            | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                          |
| UPD_TM_ZONE        | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                    |
| UPD_YMDHMS         | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）               |
| UPD_L_YMDHMS       | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）            |
| PRDG_CPNY_COD      | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                |
| PRDG_WHS_COD       | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                 |
| PRDG_CUST_COD      | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                  |
| PRDG_COD           | NVARCHAR2(15 CHAR)  | No       |              | 21        | Product Group Code / 商品グループコード                         |
| PRDG_NAM1          | NVARCHAR2(50 CHAR)  | Yes      |              | 22        | Product Group Name 1st / 商品グループ名称1st                   |
| PRDG_NAM2          | NVARCHAR2(50 CHAR)  | Yes      |              | 23        | Product Group Name 2nd / 商品グループ名称2nd                   |
| PRDG_RLOC_FIL      | NVARCHAR2(20 CHAR)  | Yes      |              | 24        | Recommended Location Filler / 推奨ロケ備考                   |
| PRDG_SPRF_DAY      | NVARCHAR2(4 CHAR)   | Yes      |              | 25        | Special Rate Date(From) / 特別料金適用期間(From)               |
| PRDG_SPRT_DAY      | NVARCHAR2(4 CHAR)   | Yes      |              | 26        | Special Rate Date(To) / 特別料金適用期間(To)                   |
| PRDG_CRCY_UOM      | CHAR(1 BYTE)        | Yes      |              | 27        | Currency UOM / 通貨単位                                    |
| PRDG_RD_DGT        | NUMBER(1,0)         | Yes      |              | 28        | Rounding Digit / 端数桁数                                  |
| PRDG_RD_KND        | CHAR(1 BYTE)        | Yes      |              | 29        | Rounding Kind / 端数処理区分                                 |
| PRDG_CALC_AVF_UNT  | CHAR(1 BYTE)        | Yes      |              | 30        | Calculation Unit Arrived Fee / 計算単位_入庫料                |
| PRDG_CALC_AVF_RD   | CHAR(2 BYTE)        | Yes      |              | 31        | Calculation Rounding  Arrived Fee / 計算端数_入庫料           |
| PRDG_PRC_AV_FEE    | NUMBER(14,4)        | Yes      |              | 32        | Price Arrived Fee / 単価_入庫料                             |
| PRDG_SPRC_AV_FEE   | NUMBER(14,4)        | Yes      |              | 33        | Special Price Arrived Fee / 特別単価_入庫料                   |
| PRDG_CALC_SPF_UNT  | CHAR(1 BYTE)        | Yes      |              | 34        | Calculation Unit Shipped Fee / 計算単位_出庫料                |
| PRDG_CALC_SPF_RD   | CHAR(2 BYTE)        | Yes      |              | 35        | Calculation Rounding  Shipped Fee / 計算端数_出庫料           |
| PRDG_PRC_SP_FEE    | NUMBER(14,4)        | Yes      |              | 36        | Price Shipped Fee / 単価_出庫料                             |
| PRDG_SPRC_SP_FEE   | NUMBER(14,4)        | Yes      |              | 37        | Special Price Shipped Fee / 特別単価_出庫料                   |
| PRDG_CALC_STF_UNT  | CHAR(1 BYTE)        | Yes      |              | 38        | Calculation Unit Storage Fee / 計算単位_保管料                |
| PRDG_CALC_STF_RD   | CHAR(2 BYTE)        | Yes      |              | 39        | Calculation Rounding  Storage Fee / 計算端数_保管料           |
| PRDG_PRC_ST_FEE    | NUMBER(14,4)        | Yes      |              | 40        | Price Storage Fee / 単価_保管料                             |
| PRDG_SPRC_ST_FEE   | NUMBER(14,4)        | Yes      |              | 41        | Special Price Storage Fee / 特別単価_保管料                   |
| PRDG_CALC_KTF_UNT  | CHAR(1 BYTE)        | Yes      |              | 42        | Calculation Unit Kitting / 計算単位_セット組                   |
| PRDG_CALC_KTF_RD   | CHAR(2 BYTE)        | Yes      |              | 43        | Calculation Rounding  Kitting / 計算端数_セット組              |
| PRDG_PRC_KT_FEE    | NUMBER(14,4)        | Yes      |              | 44        | Price Kitting / 単価_セット組                                |
| PRDG_SPRC_KT_FEE   | NUMBER(14,4)        | Yes      |              | 45        | Special Price Kitting / 特別単価_セット組                      |
| PRDG_CALC_SBF_UNT  | CHAR(1 BYTE)        | Yes      |              | 46        | Calculation Unit Set Break / 計算単位_セット解体                |
| PRDG_CALC_SBF_RD   | CHAR(2 BYTE)        | Yes      |              | 47        | Calculation Rounding  Set Break / 計算端数_セット解体           |
| PRDG_PRC_SB_FEE    | NUMBER(14,4)        | Yes      |              | 48        | Price Set Break / 単価_セット解体                             |
| PRDG_SPRC_SB_FEE   | NUMBER(14,4)        | Yes      |              | 49        | Special Price Set Break / 特別単価_セット解体                   |
| PRDG_CALC_AWF_UNT  | CHAR(1 BYTE)        | Yes      |              | 50        | Calculation Unit Attached Work Fee / 計算単位_付属作業料        |
| PRDG_CALC_AWF_RD   | CHAR(2 BYTE)        | Yes      |              | 51        | Calculation Rounding  Attached Work Fee / 計算端数_付属作業料   |
| PRDG_PRC_AW_FEE    | NUMBER(14,4)        | Yes      |              | 52        | Price Attached Work Fee / 単価_付属作業料                     |
| PRDG_SPRC_AW_FEE   | NUMBER(14,4)        | Yes      |              | 53        | Special Price Attached Work Fee / 特別単価_付属作業料           |
| PRDG_SCAI_KND      | NVARCHAR2(2 CHAR)   | Yes      |              | 54        | Shape control in Arrival Inspection / 入荷検品荷姿制御区分       |
| PRDG_SCPK_KND      | NVARCHAR2(2 CHAR)   | Yes      |              | 55        | Shape control in Picking / ピッキング荷姿制御区分                 |
| PRDG_SCSI_KND      | NVARCHAR2(2 CHAR)   | Yes      |              | 56        | Shape control in Shipping Inspection / 出荷検品荷姿制御区分      |
| PRDG_SCST_KND      | NVARCHAR2(2 CHAR)   | Yes      |              | 57        | Shape control in Storage / 庫内作業荷姿制御区分                  |
| PRDG_SCLC_KND      | NVARCHAR2(2 CHAR)   | Yes      |              | 58        | Shape control in Location Entry / 棚付荷姿制御区分             |
| PRDG_CUAI_KND      | NVARCHAR2(1 CHAR)   | Yes      |              | 59        | Count-up Kind in Arrival Inspection / 入荷検品カウントアップ区分    |
| PRDG_CUPK_KND      | NVARCHAR2(1 CHAR)   | Yes      |              | 60        | Count-up Kind in Picking / ピッキングカウントアップ区分              |
| PRDG_CUSI_KND      | NVARCHAR2(1 CHAR)   | Yes      |              | 61        | Count-up Kind in Shipping Inspection / 出荷検品カウントアップ区分   |
| PRDG_CUST_KND      | NVARCHAR2(1 CHAR)   | Yes      |              | 62        | Count-up Kind in Storage / 庫内作業カウントアップ区分               |
| PRDG_CULC_KND      | NVARCHAR2(1 CHAR)   | Yes      |              | 63        | Count-up Kind in Location / 棚付カウントアップ区分                |
| PRDG_CFG1_FLG      | CHAR(1 BYTE)        | No       | N            | 64        | Customized Flag 01 / 顧客専用フラグ01                         |
| PRDG_CFG2_FLG      | CHAR(1 BYTE)        | No       | N            | 65        | Customized Flag 02 / 顧客専用フラグ02                         |
| PRDG_CFG3_FLG      | CHAR(1 BYTE)        | No       | N            | 66        | Customized Flag 03 / 顧客専用フラグ03                         |
| PRDG_CFG4_FLG      | CHAR(1 BYTE)        | No       | N            | 67        | Customized Flag 04 / 顧客専用フラグ04                         |
| PRDG_CFG5_FLG      | CHAR(1 BYTE)        | No       | N            | 68        | Customized Flag 05 / 顧客専用フラグ05                         |
| PRDG_CFG6_FLG      | CHAR(1 BYTE)        | No       | N            | 69        | Customized Flag 06 / 顧客専用フラグ06                         |
| PRDG_CFG7_FLG      | CHAR(1 BYTE)        | No       | N            | 70        | Customized Flag 07 / 顧客専用フラグ07                         |
| PRDG_CFG8_FLG      | CHAR(1 BYTE)        | No       | N            | 71        | Customized Flag 08 / 顧客専用フラグ08                         |
| PRDG_CFG9_FLG      | CHAR(1 BYTE)        | No       | N            | 72        | Customized Flag 09 / 顧客専用フラグ09                         |
| PRDG_CFG10_FLG     | CHAR(1 BYTE)        | No       | N            | 73        | Customized Flag 10 / 顧客専用フラグ10                         |
| PRDG_CFG11_FLG     | CHAR(1 BYTE)        | No       | N            | 74        | Customized Flag 11 / 顧客専用フラグ11                         |
| PRDG_CFG12_FLG     | CHAR(1 BYTE)        | No       | N            | 75        | Customized Flag 12 / 顧客専用フラグ12                         |
| PRDG_CFG13_FLG     | CHAR(1 BYTE)        | No       | N            | 76        | Customized Flag 13 / 顧客専用フラグ13                         |
| PRDG_CFG14_FLG     | CHAR(1 BYTE)        | No       | N            | 77        | Customized Flag 14 / 顧客専用フラグ14                         |
| PRDG_CFG15_FLG     | CHAR(1 BYTE)        | No       | N            | 78        | Customized Flag 15 / 顧客専用フラグ15                         |
| PRDG_CNI1_NUM      | NUMBER(9,0)         | Yes      |              | 79        | Customized Number Item 01 / 顧客専用数字項目01                 |
| PRDG_CNI2_NUM      | NUMBER(9,0)         | Yes      |              | 80        | Customized Number Item 02 / 顧客専用数字項目02                 |
| PRDG_CNI3_NUM      | NUMBER(9,0)         | Yes      |              | 81        | Customized Number Item 03 / 顧客専用数字項目03                 |
| PRDG_CNI4_NUM      | NUMBER(9,0)         | Yes      |              | 82        | Customized Number Item 04 / 顧客専用数字項目04                 |
| PRDG_CNI5_NUM      | NUMBER(9,0)         | Yes      |              | 83        | Customized Number Item 05 / 顧客専用数字項目05                 |
| PRDG_CNI6_NUM      | NUMBER(9,0)         | Yes      |              | 84        | Customized Number Item 06 / 顧客専用数字項目06                 |
| PRDG_CNI7_NUM      | NUMBER(9,0)         | Yes      |              | 85        | Customized Number Item 07 / 顧客専用数字項目07                 |
| PRDG_CNI8_NUM      | NUMBER(9,0)         | Yes      |              | 86        | Customized Number Item 08 / 顧客専用数字項目08                 |
| PRDG_CNI9_NUM      | NUMBER(9,0)         | Yes      |              | 87        | Customized Number Item 09 / 顧客専用数字項目09                 |
| PRDG_CNI10_NUM     | NUMBER(14,4)        | Yes      |              | 88        | Customized Number Item 10 / 顧客専用数字項目10                 |
| PRDG_CNI11_NUM     | NUMBER(14,4)        | Yes      |              | 89        | Customized Number Item 11 / 顧客専用数字項目11                 |
| PRDG_CNI12_NUM     | NUMBER(14,4)        | Yes      |              | 90        | Customized Number Item 12 / 顧客専用数字項目12                 |
| PRDG_CNI13_NUM     | NUMBER(14,4)        | Yes      |              | 91        | Customized Number Item 13 / 顧客専用数字項目13                 |
| PRDG_CNI14_NUM     | NUMBER(14,4)        | Yes      |              | 92        | Customized Number Item 14 / 顧客専用数字項目14                 |
| PRDG_CNI15_NUM     | NUMBER(14,4)        | Yes      |              | 93        | Customized Number Item 15 / 顧客専用数字項目15                 |
| PRDG_CIT1          | NVARCHAR2(100 CHAR) | Yes      |              | 94        | Customized Item 01 / 顧客専用項目01                          |
| PRDG_CIT2          | NVARCHAR2(100 CHAR) | Yes      |              | 95        | Customized Item 02 / 顧客専用項目02                          |
| PRDG_CIT3          | NVARCHAR2(100 CHAR) | Yes      |              | 96        | Customized Item 03 / 顧客専用項目03                          |
| PRDG_CIT4          | NVARCHAR2(100 CHAR) | Yes      |              | 97        | Customized Item 04 / 顧客専用項目04                          |
| PRDG_CIT5          | NVARCHAR2(100 CHAR) | Yes      |              | 98        | Customized Item 05 / 顧客専用項目05                          |
| PRDG_CIT6          | NVARCHAR2(100 CHAR) | Yes      |              | 99        | Customized Item 06 / 顧客専用項目06                          |
| PRDG_CIT7          | NVARCHAR2(100 CHAR) | Yes      |              | 100       | Customized Item 07 / 顧客専用項目07                          |
| PRDG_CIT8          | NVARCHAR2(100 CHAR) | Yes      |              | 101       | Customized Item 08 / 顧客専用項目08                          |
| PRDG_CIT9          | NVARCHAR2(100 CHAR) | Yes      |              | 102       | Customized Item 09 / 顧客専用項目09                          |
| PRDG_CIT10         | NVARCHAR2(100 CHAR) | Yes      |              | 103       | Customized Item 10 / 顧客専用項目10                          |
| PRDG_CIT11         | NVARCHAR2(100 CHAR) | Yes      |              | 104       | Customized Item 11 / 顧客専用項目11                          |
| PRDG_CIT12         | NVARCHAR2(100 CHAR) | Yes      |              | 105       | Customized Item 12 / 顧客専用項目12                          |
| PRDG_CIT13         | NVARCHAR2(100 CHAR) | Yes      |              | 106       | Customized Item 13 / 顧客専用項目13                          |
| PRDG_CIT14         | NVARCHAR2(100 CHAR) | Yes      |              | 107       | Customized Item 14 / 顧客専用項目14                          |
| PRDG_CIT15         | NVARCHAR2(100 CHAR) | Yes      |              | 108       | Customized Item 15 / 顧客専用項目15                          |
| PRDG_TEMP_CTR      | CHAR(2 BYTE)        | Yes      |              | 109       | Temperature Control                                    |
| PRDG_INBO_KND      | CHAR(2 BYTE)        | Yes      |              | 110       | Inbound Method                                         |
| PRDG_EXPR_KND      | CHAR(1 BYTE)        | Yes      |              | 111       | Expiration Date Of Product Kind                        |
| PRDG_EXPR_DAY      | NUMBER(3,0)         | Yes      |              | 112       | Product Expire Due Date                                |
| PRDG_BBPR_KND      | CHAR(1 BYTE)        | Yes      |              | 113       | Best Before Date Of Product Kind                       |
| PRDG_BBPR_DAY      | NUMBER(3,0)         | Yes      |              | 114       | Best Before Due Date                                   |
| PRDG_QC_SKP_FLG    | CHAR(1 BYTE)        | No       | Y            | 115       | Process QC Skip Flag                                   |
| PRDG_LOT1_ALPT_KND | CHAR(1 BYTE)        | Yes      |              | 116       | Lot 1 Allocation Pattern Kind                          |
| PRDG_LOT1_PIK_KEY  | NVARCHAR2(50 CHAR)  | Yes      |              | 117       | Lot 1 Picking Key                                      |
| PRDG_LOT2_ALPT_KND | CHAR(1 BYTE)        | Yes      |              | 118       | Lot 2 Allocation Pattern Kind                          |
| PRDG_LOT2_PIK_KEY  | NVARCHAR2(50 CHAR)  | Yes      |              | 119       | Lot 2 Picking Key                                      |
| PRDG_TEMP_FROM     | NVARCHAR2(10 CHAR)  | Yes      |              | 120       | Temperature Range From                                 |
| PRDG_TEMP_TO       | NVARCHAR2(10 CHAR)  | Yes      |              | 121       | Temperature Range To                                   |
| PRDG_ADIF_SKU_FLG  | CHAR(1 BYTE)        | No       | Y            | 122       | Allow Different SKUs With Same Barcode In One Location |
| PRDG_VPT_FLG       | CHAR(1 BYTE)        | No       | N            | 123       | VAS Price  TAG                                         |
| PRDG_VLD_FLG       | CHAR(1 BYTE)        | No       | N            | 124       | VAS Legal Desc                                         |
| PRDG_VFL_FLG       | CHAR(1 BYTE)        | No       | N            | 125       | VAS Free Label                                         |
| PRDG_SRT_KND       | CHAR(2 BYTE)        | Yes      |              | 126       | Sort Method Kind                                       |
| PRDG_RCV_KND       | CHAR(2 BYTE)        | Yes      |              | 127       | Receive Method Kind                                    |
| PRDG_STW_KND       | CHAR(2 BYTE)        | Yes      |              | 128       | Stowing Method Kind                                    |
| PRDG_PIK_KND       | CHAR(2 BYTE)        | Yes      |              | 129       | Picking Method Kind                                    |
| PRDG_AST_KND       | CHAR(2 BYTE)        | Yes      |              | 130       | Assort  Method Kind                                    |
| PRDG_QC_KND        | CHAR(2 BYTE)        | Yes      |              | 131       | QC Method Kind                                         |
| PRDG_CUSR_KND      | NVARCHAR2(1 CHAR)   | Yes      |              | 132       | Count-up Kind in Sorting Inspection                    |
| PRDG_SCSR_KND      | NVARCHAR2(2 CHAR)   | Yes      |              | 133       | Shape control in Sorting / 仕分荷姿制御区分                    |
