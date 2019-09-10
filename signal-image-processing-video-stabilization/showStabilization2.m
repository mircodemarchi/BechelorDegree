function [new, ccs] = showStabilization2(frames, templateIndex)
%SHOWSTABILIZATION2 Esegue e mostra statistiche per la stabilizzazione di 
%                   un video di frame rispetto a un template scelto da un 
%                   frame del video. 
%
% La stabilizzazione avviene rispetto al template che viene ritagliato dal 
% video, ovvero il video risultante avrà l'immagine ricavata dalla 
% correlazione di ogni frame del video originale e del template allineata
% nello stesso identico punto in cui è avvenuto inizialmente il crop del
% template.
%
%  INPUT
%   frames: Video di frame da stabilizzare;
%   templateIndex: Indice del frame dal quale prendere il template per fare
%                  la stabilizzazione;
%  OUTPUT
%   new: Risultato della stabilizzazione;
%   ccs: Grafico della Cross Correlazione 2D del video di frame con
%        template.
%

% Ricavo il template ritagliando il frame sul video a indice templateIndex
figure; template = imcrop(frames(:,:,:,templateIndex));
template = rgb2gray(template);

% Per poter fare la stabilizzazione rispetto al crop, devo ricavare
% l'offset del template rispetto al frame del video su indice
% templateIndex. Per farlo eseguo la Cross Correlazione 2D tra il template
% e il frame del video su indice templateIndex. In questo modo ricavo le
% coordinate del punto massimo di correlazione, che corrisponderà 
% sicuramente al mio template, dal quale ottengo l'offset.
template_cc = normxcorr2(template, rgb2gray(frames(:,:,:,templateIndex)));
[~, imax] = max((template_cc(:)));
[ypeak, xpeak] = ind2sub(size(template_cc),imax);
template_offset = [(ypeak-size(template,1)) (xpeak-size(template,2))];

% Scorro ogni frame del video
figure('Renderer', 'painters', 'Position', [300 200 900 600]);
for i=1:size(frames,4)
    % Eseguo la cross correlazione 2D su ogni frame usando template
    comp  = frames(:,:,:,i);
    compg = rgb2gray(comp);
    cc = normxcorr2(template,compg);
    
    % Calcolo il punto di massima correlazione tra le due immagini e le
    % coordinate di questo punto
    [max_cc, imax] = max((cc(:)));
    [ypeak, xpeak] = ind2sub(size(cc),imax);

    % Calcolo l'offset del template sul singolo frame
    corr_offset = [(ypeak-size(template,1)) (xpeak-size(template,2))];
    %new(:,:,:,i) = imtranslate(frames(:,:,:,i),[-(corr_offset(2)+round(C/2)), -(corr_offset(1))],'FillValues',0);
    
    % Eseguo la traslazione del frame riportando al centro l'immagine 
    % ricavata dalla cross correlazione:
    %  1) Tolgo l'offset appena ricavato così da allineare il vertice in 
    %     alto a sinistra del template trovato dalla cross correlazione
    %     con il frame, con il vertice in alto a sinistra del frame;
    %  2) Aggiungo l'offset ricavato precedentemente del template rispetto
    %     al frame dal quale ho estratto il template, così da ottenere
    %     l'immagine stabilizzata esattamente sulla posizione in cui avevo
    %     precedentemente effettuato il crop.
    % 
    new(:,:,:,i) = imtranslate(frames(:,:,:,i),[-corr_offset(2) + template_offset(2), -corr_offset(1) + template_offset(1)],'FillValues',0);
    ccs(:,:,i) = cc;

    % Mostro il template scelto, l'immagine originale, il grafico di cross
    % correlazione e l'immagine stabilizzata.
    subplot(221); imshow(template); axis image; title(strcat('Template scelto n°', num2str(templateIndex)));
    subplot(222); imshow(frames(:,:,:,i)); axis image;  title(strcat('Immagine originale: ', num2str(i)));
    subplot(223); imagesc(ccs(:,:,i)); colorbar; title({'Mappa di cross-correlazione 2D', strcat('MaxCCValue:', num2str(max_cc))});
    hold on;      scatter(xpeak, ypeak,'rX');
    subplot(224); imshow(new(:,:,:,i)); title('Immagine stabilizzata');
   
    %ref=new(:,:,:,i);
    pause(0.1)
end

end
