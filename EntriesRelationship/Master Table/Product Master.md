# GWH_TM_PROD
#Entity #Standard #MASTER 

| COLUMN_NAME         | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                                          |
|---------------------|---------------------|----------|--------------|-----------|-------------------------------------------------------------------|
| DEL_FLG             | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                             |
| CRT_YMD             | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                                      |
| CRT_TIM             | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                                     |
| CRT_TMID            | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                           |
| CRT_USER            | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                                          |
| CRT_PGM             | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                                     |
| CRT_TM_ZONE         | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                               |
| CRT_YMDHMS          | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）                          |
| CRT_L_YMDHMS        | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）                       |
| UPD_YMD             | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                                      |
| UPD_TIM             | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                                     |
| UPD_TMID            | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                                       |
| UPD_USER            | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                                          |
| UPD_PGM             | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                                     |
| UPD_TM_ZONE         | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                               |
| UPD_YMDHMS          | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）                          |
| UPD_L_YMDHMS        | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）                       |
| PROD_CPNY_COD       | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                           |
| PROD_WHS_COD        | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                            |
| PROD_CUST_COD       | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                             |
| PROD_COD            | NVARCHAR2(50 CHAR)  | No       |              | 21        | Product Code / 商品コード                                              |
| PROD_NAM1           | NVARCHAR2(50 CHAR)  | Yes      |              | 22        | Product Name 1St / 商品名称1st                                        |
| PROD_NAM2           | NVARCHAR2(50 CHAR)  | Yes      |              | 23        | Product Name 2Nd / 商品名称2nd                                        |
| PROD_NAM3           | NVARCHAR2(50 CHAR)  | Yes      |              | 24        | Product Name 3Rd / 商品名称3rd                                        |
| PROD_NAM4           | NVARCHAR2(50 CHAR)  | Yes      |              | 25        | Product Name 4Th / 商品名称4th                                        |
| PROD_PRDG_COD       | NVARCHAR2(15 CHAR)  | Yes      |              | 26        | Product Group Code / 商品グループコード                                    |
| PROD_PPC_NUM        | NUMBER(9,0)         | No       |              | 27        | Number Of Pieces Per Case / ケース入数                                 |
| PROD_ALCT_KBN       | CHAR(1 BYTE)        | Yes      |              | 28        | Default Allocated Method Kind / 引当タイプ初期値区分                        |
| PROD_STCK_FLG       | CHAR(1 BYTE)        | No       |              | 29        | Stock Control Flag / 出荷対象外フラグ                                     |
| PROD_HRC1           | NVARCHAR2(30 CHAR)  | Yes      |              | 30        | Hierarchy1 / 商品階層1                                                |
| PROD_HRC2           | NVARCHAR2(30 CHAR)  | Yes      |              | 31        | Hierarchy2 / 商品階層2                                                |
| PROD_HRC3           | NVARCHAR2(30 CHAR)  | Yes      |              | 32        | Hierarchy3 / 商品階層3                                                |
| PROD_DPST_AMT       | NUMBER(14,4)        | Yes      |              | 33        | Deposit Price / 寄託価格                                              |
| PROD_DPCR_UNT       | CHAR(3 BYTE)        | Yes      |              | 34        | Deposit Price Currency / 寄託価格_通貨単位                                |
| PROD_PCHS_AMT       | NUMBER(14,4)        | Yes      |              | 35        | Purchase Price / 仕入価格                                             |
| PROD_PCCR_UNT       | CHAR(3 BYTE)        | Yes      |              | 36        | Purchase Price Currency / 仕入価格_通貨単位                               |
| PROD_SLS_AMT        | NUMBER(14,4)        | Yes      |              | 37        | Sales Price / 売上価格                                                |
| PROD_SLCR_UNT       | CHAR(3 BYTE)        | Yes      |              | 38        | Sales Price Currency / 売上価格_通貨単位                                  |
| PROD_CS_WGT         | NUMBER(12,6)        | Yes      |              | 39        | Case Weight / ケース重量                                               |
| PROD_CS_M3          | NUMBER(12,6)        | Yes      |              | 40        | Case Volume / ケース容積                                               |
| PROD_CS_LEN         | NUMBER(12,6)        | Yes      |              | 41        | Case Length / ケース縦                                                |
| PROD_CS_WID         | NUMBER(12,6)        | Yes      |              | 42        | Case Width / ケース横                                                 |
| PROD_CS_HIG         | NUMBER(12,6)        | Yes      |              | 43        | Case High / ケース高さ                                                 |
| PROD_PC_WGT         | NUMBER(12,6)        | Yes      |              | 44        | Piece Weight / バラ重量                                               |
| PROD_PC_M3          | NUMBER(12,6)        | Yes      |              | 45        | Piece Volume / バラ容積                                               |
| PROD_PC_LEN         | NUMBER(12,6)        | Yes      |              | 46        | Piece Length / バラ縦                                                |
| PROD_PC_WID         | NUMBER(12,6)        | Yes      |              | 47        | Piece Width / バラ横                                                 |
| PROD_PC_HIG         | NUMBER(12,6)        | Yes      |              | 48        | Piece High / バラ高さ                                                 |
| PROD_PCUM_PCS       | NVARCHAR2(3 CHAR)   | Yes      |              | 49        | UOM(Piece) / UOM(バラ数)                                             |
| PROD_PCUM_CS        | NVARCHAR2(3 CHAR)   | Yes      |              | 50        | UOM(Case) / UOM(ケース数)                                             |
| PROD_PCUM_HIG       | NVARCHAR2(3 CHAR)   | Yes      |              | 51        | UOM(Length) / UOM（長さ）                                             |
| PROD_PCUM_WGT       | NVARCHAR2(3 CHAR)   | Yes      |              | 52        | UOM(Weight) / UOM（重量）                                             |
| PROD_RCAR_COD       | CHAR(3 BYTE)        | Yes      |              | 53        | Recomended Area / 推奨エリア                                           |
| PROD_RCRA_COD       | NVARCHAR2(10 CHAR)  | Yes      |              | 54        | Recomended Rack / 推奨ラック                                           |
| PROD_RCPS_COD       | NVARCHAR2(3 CHAR)   | Yes      |              | 55        | Recomended Position / 推奨ポジション                                     |
| PROD_RCLV_COD       | NVARCHAR2(2 CHAR)   | Yes      |              | 56        | Recomended Level / 推奨レベル                                          |
| PROD_RPAR_COD       | CHAR(3 BYTE)        | Yes      |              | 57        | Replenishment Recomended Area / 補充先推奨エリア                          |
| PROD_RPRA_COD       | NVARCHAR2(10 CHAR)  | Yes      |              | 58        | Replenishment Rack Code / 補充先推奨ラック                                |
| PROD_RPPS_COD       | NVARCHAR2(3 CHAR)   | Yes      |              | 59        | Replenishment Position Code / 補充先推奨ポジション                          |
| PROD_RPLV_COD       | NVARCHAR2(2 CHAR)   | Yes      |              | 60        | Replenishment Level Code / 補充先推奨レベル                               |
| PROD_RMXP_QTY       | NUMBER(9,0)         | Yes      |              | 61        | Replenishment Max Piece Quantity Stock To Pick / 補充_定期最大補充数量_バラ   |
| PROD_RSDP_QTY       | NUMBER(9,0)         | Yes      |              | 62        | Replenishment Minimum Piece Quantity Stock To Pick / 補充_基準在庫数量_バラ |
| PROD_GRS_WGT        | NUMBER(12,6)        | Yes      |              | 63        | Gross Weight / 実重量                                                |
| PROD_NET_WGT        | NUMBER(12,6)        | Yes      |              | 64        | Net Weight / 正味重量                                                 |
| PROD_ACSR_FLG       | CHAR(1 BYTE)        | No       |              | 65        | Accessary Flag / 付属品有フラグ                                          |
| PROD_STWG_QTY       | NUMBER(5,0)         | Yes      |              | 66        | Case (Per PLT) / パレット積付ケース数                                       |
| PROD_FRTY_KND       | CHAR(2 BYTE)        | Yes      |              | 67        | Japan Standard Industrial Classification / 40品目区分                 |
| PROD_AITM_KND       | CHAR(2 BYTE)        | No       |              | 68        | Arrow(NEC service) Item Kind / アロー品目区分                            |
| PROD_AEBG_KND       | CHAR(2 BYTE)        | No       |              | 69        | Arrow(NEC service) Extra Big Name Kind / アロー特大品目区分                |
| PROD_HS_COD         | NVARCHAR2(18 CHAR)  | Yes      |              | 70        | HS CODE / HS CODE                                                 |
| PROD_BOND_KND       | CHAR(2 BYTE)        | No       |              | 71        | Bond Control Kind / 保税管理区分                                        |
| PROD_CUST_STS       | NVARCHAR2(2 CHAR)   | Yes      |              | 72        | Customs Status / CUSTOMS_STATUS                                   |
| PROD_STRG_KND       | CHAR(2 BYTE)        | No       |              | 73        | Storage Kind / 保管区分                                               |
| PROD_SPFQ           | CHAR(1 BYTE)        | Yes      |              | 74        | Shipping Frequency / 出荷頻度(ABC分析用)                                 |
| PROD_SPDU_DAY       | NUMBER(5,0)         | Yes      |              | 75        | Shipping Due Date / 出荷期限日数                                        |
| PROD_SPDU_KND       | CHAR(2 BYTE)        | No       |              | 76        | Due Date Of Shipping Unit Kind / 出荷期限日数単位区分                       |
| PROD_STDU_DAY       | NUMBER(5,0)         | Yes      |              | 77        | Stock Due Date / 在庫期限日数                                           |
| PROD_STDU_KND       | CHAR(2 BYTE)        | No       |              | 78        | Due Date Of Stock Unit Kind / 在庫期限日数単位区分                          |
| PROD_EXPR_KND       | CHAR(1 BYTE)        | Yes      |              | 79        | Expiration Date Of Product Kind / 有効期限区分                          |
| PROD_EXPR_DAY       | NUMBER(5,0)         | Yes      |              | 80        | Product Expire Due Date / 商品期限日数                                  |
| PROD_CCGP           | NVARCHAR2(20 CHAR)  | Yes      |              | 81        | Cycle Count Group / サイクルカウントグループ                                  |
| PROD_KIT_FLG        | CHAR(1 BYTE)        | No       |              | 82        | Kitting Product Flag / セット商品親フラグ                                  |
| PROD_KITS_FLG       | CHAR(1 BYTE)        | No       |              | 83        | Kitting Product Storage Conrol Flag / セット商品在庫管理フラグ                |
| PROD_XDOC_KND       | CHAR(2 BYTE)        | Yes      |              | 84        | Cross Dock Kind / クロスドック区分                                        |
| PROD_PSSA_KND       | CHAR(2 BYTE)        | No       |              | 85        | PSSA Kind / 成体管理区分                                                |
| PROD_PSSA_DAY       | NUMBER(4,0)         | Yes      |              | 86        | PSSA Expire Date / 成体管理期限日数                                       |
| PROD_SPRF_DAY       | NVARCHAR2(4 CHAR)   | Yes      |              | 87        | Special Rate Date(From) / 特別料金適用期間(From)                          |
| PROD_SPRT_DAY       | NVARCHAR2(4 CHAR)   | Yes      |              | 88        | Special Rate Date(To) / 特別料金適用期間(To)                              |
| PROD_CRCY_UOM       | CHAR(1 BYTE)        | Yes      |              | 89        | Currency UOM / 通貨単位                                               |
| PROD_RD_DGT         | NUMBER(1,0)         | Yes      |              | 90        | Rounding Digit / 端数桁数                                             |
| PROD_RD_KND         | CHAR(1 BYTE)        | Yes      |              | 91        | Rounding Kind / 端数処理区分                                            |
| PROD_CALC_AVF_UNT   | CHAR(1 BYTE)        | Yes      |              | 92        | Calculation Unit Arrived Fee / 計算単位_入庫料                           |
| PROD_CALC_AVF_RD    | CHAR(2 BYTE)        | Yes      |              | 93        | Calculation Rounding  Arrived Fee / 計算端数_入庫料                      |
| PROD_PRC_AV_FEE     | NUMBER(14,4)        | Yes      |              | 94        | Price Arrived Fee / 単価_入庫料                                        |
| PROD_SPRC_AV_FEE    | NUMBER(14,4)        | Yes      |              | 95        | Special Price Arrived Fee / 特別単価_入庫料                              |
| PROD_CALC_SPF_UNT   | CHAR(1 BYTE)        | Yes      |              | 96        | Calculation Unit Shipped Fee / 計算単位_出庫料                           |
| PROD_CALC_SPF_RD    | CHAR(2 BYTE)        | Yes      |              | 97        | Calculation Rounding  Shipped Fee / 計算端数_出庫料                      |
| PROD_PRC_SP_FEE     | NUMBER(14,4)        | Yes      |              | 98        | Price Shipped Fee / 単価_出庫料                                        |
| PROD_SPRC_SP_FEE    | NUMBER(14,4)        | Yes      |              | 99        | Special Price Shipped Fee / 特別単価_出庫料                              |
| PROD_CALC_STF_UNT   | CHAR(1 BYTE)        | Yes      |              | 100       | Calculation Unit Storage Fee / 計算単位_保管料                           |
| PROD_CALC_STF_RD    | CHAR(2 BYTE)        | Yes      |              | 101       | Calculation Rounding  Storage Fee / 計算端数_保管料                      |
| PROD_PRC_ST_FEE     | NUMBER(14,4)        | Yes      |              | 102       | Price Storage Fee / 単価_保管料                                        |
| PROD_SPRC_ST_FEE    | NUMBER(14,4)        | Yes      |              | 103       | Special Price Storage Fee / 特別単価_保管料                              |
| PROD_CALC_KTF_UNT   | CHAR(1 BYTE)        | Yes      |              | 104       | Calculation Unit Kitting / 計算単位_セット組                              |
| PROD_CALC_KTF_RD    | CHAR(2 BYTE)        | Yes      |              | 105       | Calculation Rounding  Kitting / 計算端数_セット組                         |
| PROD_PRC_KT_FEE     | NUMBER(14,4)        | Yes      |              | 106       | Price Kitting / 単価_セット組                                           |
| PROD_SPRC_KT_FEE    | NUMBER(14,4)        | Yes      |              | 107       | Special Price Kitting / 特別単価_セット組                                 |
| PROD_CALC_SBF_UNT   | CHAR(1 BYTE)        | Yes      |              | 108       | Calculation Unit Set Break / 計算単位_セット解体                           |
| PROD_CALC_SBF_RD    | CHAR(2 BYTE)        | Yes      |              | 109       | Calculation Rounding  Set Break / 計算端数_セット解体                      |
| PROD_PRC_SB_FEE     | NUMBER(14,4)        | Yes      |              | 110       | Price Set Break / 単価_セット解体                                        |
| PROD_SPRC_SB_FEE    | NUMBER(14,4)        | Yes      |              | 111       | Special Price Set Break / 特別単価_セット解体                              |
| PROD_CALC_AWF_UNT   | CHAR(1 BYTE)        | Yes      |              | 112       | Calculation Unit Attached Work Fee / 計算単位_付属作業料                   |
| PROD_CALC_AWF_RD    | CHAR(2 BYTE)        | Yes      |              | 113       | Calculation Rounding  Attached Work Fee / 計算端数_付属作業料              |
| PROD_PRC_AW_FEE     | NUMBER(14,4)        | Yes      |              | 114       | Price Attached Work Fee / 単価_付属作業料                                |
| PROD_SPRC_AW_FEE    | NUMBER(14,4)        | Yes      |              | 115       | Special Price Attached Work Fee / 特別単価_付属作業料                      |
| PROD_PRMX_QTY       | NUMBER(9,0)         | Yes      |              | 116       | Proper Maximum PCS / 適正在庫数量_最大値                                   |
| PROD_PRMN_QTY       | NUMBER(9,0)         | Yes      |              | 117       | Proper Minimum PCS / 適正在庫数量_最小値                                   |
| PROD_SLGT_FLG       | CHAR(1 BYTE)        | No       | N            | 118       | Serial/LOT Get Product Flag / Serial/Lot採取対象商品フラグ                 |
| PROD_CLI1           | NVARCHAR2(50 CHAR)  | Yes      |              | 119       | Used for only customization01 / 顧客専用の帳票やファイル入出力用の項目01             |
| PROD_CLI2           | NVARCHAR2(50 CHAR)  | Yes      |              | 120       | Used for only customization02 / 顧客専用の帳票やファイル入出力用の項目02             |
| PROD_CLI3           | NVARCHAR2(50 CHAR)  | Yes      |              | 121       | Used for only customization03 / 顧客専用の帳票やファイル入出力用の項目03             |
| PROD_CLI4           | NVARCHAR2(50 CHAR)  | Yes      |              | 122       | Used for only customization04 / 顧客専用の帳票やファイル入出力用の項目04             |
| PROD_CLI5           | NVARCHAR2(50 CHAR)  | Yes      |              | 123       | Used for only customization05 / 顧客専用の帳票やファイル入出力用の項目05             |
| PROD_CLI6           | NVARCHAR2(50 CHAR)  | Yes      |              | 124       | Used for only customization06 / 顧客専用の帳票やファイル入出力用の項目06             |
| PROD_CLI7           | NVARCHAR2(50 CHAR)  | Yes      |              | 125       | Used for only customization07 / 顧客専用の帳票やファイル入出力用の項目07             |
| PROD_CLI8           | NVARCHAR2(50 CHAR)  | Yes      |              | 126       | Used for only customization08 / 顧客専用の帳票やファイル入出力用の項目08             |
| PROD_CLI9           | NVARCHAR2(50 CHAR)  | Yes      |              | 127       | Used for only customization09 / 顧客専用の帳票やファイル入出力用の項目09             |
| PROD_CLI10          | NVARCHAR2(50 CHAR)  | Yes      |              | 128       | Used for only customization10 / 顧客専用の帳票やファイル入出力用の項目10             |
| PROD_CLI11          | NVARCHAR2(50 CHAR)  | Yes      |              | 129       | Used for only customization11 / 顧客専用の帳票やファイル入出力用の項目11             |
| PROD_CLI12          | NVARCHAR2(50 CHAR)  | Yes      |              | 130       | Used for only customization12 / 顧客専用の帳票やファイル入出力用の項目12             |
| PROD_CLI13          | NVARCHAR2(50 CHAR)  | Yes      |              | 131       | Used for only customization13 / 顧客専用の帳票やファイル入出力用の項目13             |
| PROD_CLI14          | NVARCHAR2(50 CHAR)  | Yes      |              | 132       | Used for only customization14 / 顧客専用の帳票やファイル入出力用の項目14             |
| PROD_CLI15          | NVARCHAR2(50 CHAR)  | Yes      |              | 133       | Used for only customization15 / 顧客専用の帳票やファイル入出力用の項目15             |
| PROD_CFG1_FLG       | CHAR(1 BYTE)        | No       | N            | 134       | Used for only customization (Flag)01 / 顧客専用のフラグ01                 |
| PROD_CFG2_FLG       | CHAR(1 BYTE)        | No       | N            | 135       | Used for only customization (Flag)02 / 顧客専用のフラグ02                 |
| PROD_CFG3_FLG       | CHAR(1 BYTE)        | No       | N            | 136       | Used for only customization (Flag)03 / 顧客専用のフラグ03                 |
| PROD_CFG4_FLG       | CHAR(1 BYTE)        | No       | N            | 137       | Used for only customization (Flag)04 / 顧客専用のフラグ04                 |
| PROD_CFG5_FLG       | CHAR(1 BYTE)        | No       | N            | 138       | Used for only customization (Flag)05 / 顧客専用のフラグ05                 |
| PROD_CFG6_FLG       | CHAR(1 BYTE)        | No       | N            | 139       | Used for only customization (Flag)06 / 顧客専用のフラグ06                 |
| PROD_CFG7_FLG       | CHAR(1 BYTE)        | No       | N            | 140       | Used for only customization (Flag)07 / 顧客専用のフラグ07                 |
| PROD_CFG8_FLG       | CHAR(1 BYTE)        | No       | N            | 141       | Used for only customization (Flag)08 / 顧客専用のフラグ08                 |
| PROD_CFG9_FLG       | CHAR(1 BYTE)        | No       | N            | 142       | Used for only customization (Flag)09 / 顧客専用のフラグ09                 |
| PROD_CFG10_FLG      | CHAR(1 BYTE)        | No       | N            | 143       | Used for only customization (Flag)10 / 顧客専用のフラグ10                 |
| PROD_CFG11_FLG      | CHAR(1 BYTE)        | No       | N            | 144       | Used for only customization (Flag)11 / 顧客専用のフラグ11                 |
| PROD_CFG12_FLG      | CHAR(1 BYTE)        | No       | N            | 145       | Used for only customization (Flag)12 / 顧客専用のフラグ12                 |
| PROD_CFG13_FLG      | CHAR(1 BYTE)        | No       | N            | 146       | Used for only customization (Flag)13 / 顧客専用のフラグ13                 |
| PROD_CFG14_FLG      | CHAR(1 BYTE)        | No       | N            | 147       | Used for only customization (Flag)14 / 顧客専用のフラグ14                 |
| PROD_CFG15_FLG      | CHAR(1 BYTE)        | No       | N            | 148       | Used for only customization (Flag)15 / 顧客専用のフラグ15                 |
| PROD_CNI1_NUM       | NUMBER(9,0)         | Yes      |              | 149       | Used for only customization (Number)01 / 顧客専用の数字項目01              |
| PROD_CNI2_NUM       | NUMBER(9,0)         | Yes      |              | 150       | Used for only customization (Number)02 / 顧客専用の数字項目02              |
| PROD_CNI3_NUM       | NUMBER(9,0)         | Yes      |              | 151       | Used for only customization (Number)03 / 顧客専用の数字項目03              |
| PROD_CNI4_NUM       | NUMBER(9,0)         | Yes      |              | 152       | Used for only customization (Number)04 / 顧客専用の数字項目04              |
| PROD_CNI5_NUM       | NUMBER(9,0)         | Yes      |              | 153       | Used for only customization (Number)05 / 顧客専用の数字項目05              |
| PROD_CNI6_NUM       | NUMBER(9,0)         | Yes      |              | 154       | Used for only customization (Number)06 / 顧客専用の数字項目06              |
| PROD_CNI7_NUM       | NUMBER(9,0)         | Yes      |              | 155       | Used for only customization (Number)07 / 顧客専用の数字項目07              |
| PROD_CNI8_NUM       | NUMBER(9,0)         | Yes      |              | 156       | Used for only customization (Number)08 / 顧客専用の数字項目08              |
| PROD_CNI9_NUM       | NUMBER(9,0)         | Yes      |              | 157       | Used for only customization (Number)09 / 顧客専用の数字項目09              |
| PROD_CNI10_NUM      | NUMBER(14,4)        | Yes      |              | 158       | Used for only customization (w/ decimal)10 / 顧客専用の数字項目(小数あり)10    |
| PROD_CNI11_NUM      | NUMBER(14,4)        | Yes      |              | 159       | Used for only customization (w/ decimal)11 / 顧客専用の数字項目(小数あり)11    |
| PROD_CNI12_NUM      | NUMBER(14,4)        | Yes      |              | 160       | Used for only customization (w/ decimal)12 / 顧客専用の数字項目(小数あり)12    |
| PROD_CNI13_NUM      | NUMBER(14,4)        | Yes      |              | 161       | Used for only customization (w/ decimal)13 / 顧客専用の数字項目(小数あり)13    |
| PROD_CNI14_NUM      | NUMBER(14,4)        | Yes      |              | 162       | Used for only customization (w/ decimal)14 / 顧客専用の数字項目(小数あり)14    |
| PROD_CNI15_NUM      | NUMBER(14,4)        | Yes      |              | 163       | Used for only customization (w/ decimal)15 / 顧客専用の数字項目(小数あり)15    |
| PROD_RCV_BTCH_NUM   | NVARCHAR2(30 CHAR)  | Yes      |              | 164       | Recive Batch Number / 受信バッチNo                                     |
| PROD_PCUM_M3        | NVARCHAR2(3 CHAR)   | Yes      |              | 165       | UOM(Volume) / UOM（容積）                                             |
| PROD_PPB_NUM        | NUMBER(9,0)         | Yes      |              | 166       | Number Of Pieces Per Ball / ボール入数                                 |
| PROD_SCAI_KND       | NVARCHAR2(2 CHAR)   | Yes      |              | 167       | Shape control in Arrival Inspection / 入荷検品荷姿制御区分                  |
| PROD_SCPK_KND       | NVARCHAR2(2 CHAR)   | Yes      |              | 168       | Shape control in Picking / ピッキング荷姿制御区分                            |
| PROD_SCSI_KND       | NVARCHAR2(2 CHAR)   | Yes      |              | 169       | Shape control in Shipping Inspection / 出荷検品荷姿制御区分                 |
| PROD_SCST_KND       | NVARCHAR2(2 CHAR)   | Yes      |              | 170       | Shape control in Storage / 庫内作業荷姿制御区分                             |
| PROD_SCLC_KND       | NVARCHAR2(2 CHAR)   | Yes      |              | 171       | Shape control in Location Entry / 棚付荷姿制御区分                        |
| PROD_CUAI_KND       | NVARCHAR2(1 CHAR)   | Yes      |              | 172       | Count-up Kind in Arrival Inspection / 入荷検品カウントアップ区分               |
| PROD_CUPK_KND       | NVARCHAR2(1 CHAR)   | Yes      |              | 173       | Count-up Kind in Picking / ピッキングカウントアップ区分                         |
| PROD_CUSI_KND       | NVARCHAR2(1 CHAR)   | Yes      |              | 174       | Count-up Kind in Shipping Inspection / 出荷検品カウントアップ区分              |
| PROD_CUST_KND       | NVARCHAR2(1 CHAR)   | Yes      |              | 175       | Count-up Kind in Storage / 庫内作業数値カウントアップ区分                        |
| PROD_CULC_KND       | NVARCHAR2(1 CHAR)   | Yes      |              | 176       | Count-up Kind in Location / 棚付カウントアップ区分                           |
| PROD_AVDU_DAY       | NUMBER(5,0)         | Yes      |              | 177       | Arrival Due Date / 入荷期限日数                                         |
| PROD_AVDU_KND       | CHAR(2 BYTE)        | No       | 00           | 178       | Due Date Of Arrival Unit Kind / 入荷期限日数単位区分                        |
| PROD_PCUM_BL        | NVARCHAR2(3 CHAR)   | Yes      |              | 179       | UOM(Ball)                                                         |
| PROD_BL_WGT         | NUMBER(12,6)        | Yes      |              | 180       | Ball Weight                                                       |
| PROD_BL_M3          | NUMBER(12,6)        | Yes      |              | 181       | Ball Volume                                                       |
| PROD_BL_LEN         | NUMBER(12,6)        | Yes      |              | 182       | Ball Length                                                       |
| PROD_BL_WID         | NUMBER(12,6)        | Yes      |              | 183       | Ball Width                                                        |
| PROD_BL_HIG         | NUMBER(12,6)        | Yes      |              | 184       | Ball High                                                         |
| PROD_BBPR_KND       | CHAR(1 BYTE)        | Yes      |              | 185       | Best Before Date Of Product Kind                                  |
| PROD_BBPR_DAY       | NUMBER(3,0)         | Yes      |              | 186       | Best Before Due Date                                              |
| PROD_CASE_MARK_TYPE | NVARCHAR2(50 CHAR)  | Yes      |              | 187       | Case Mark                                                         |
| PROD_STWG_PC_QTY    | NUMBER(12,3)        | Yes      |              | 188       | Piece (Per PLT) / パレット積付ピース数                                      |
| PROD_CUSR_KND       | NVARCHAR2(1 CHAR)   | Yes      |              | 189       | Count-up Kind in Sorting Inspection / 仕分検品カウントアップ区分               |
| PROD_SCSR_KND       | NVARCHAR2(2 CHAR)   | Yes      |              | 190       | Shape control in Sorting / 仕分荷姿制御区分                               |
| PROD_SRL_ALLO_FLG   | CHAR(1 BYTE)        | No       | N            | 191       | Serial Allocation Flag                                            |
